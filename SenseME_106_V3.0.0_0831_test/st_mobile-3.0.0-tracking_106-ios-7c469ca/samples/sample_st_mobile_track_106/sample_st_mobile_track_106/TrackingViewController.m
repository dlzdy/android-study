//
//  TrackViewController.m
//  sample_st_mobile_track_106
//
//  Created by sluin on 16/5/11.
//  Copyright © 2016年 SenseTime. All rights reserved.
//

#import "TrackingViewController.h"
#import <CommonCrypto/CommonDigest.h>
#import <AVFoundation/AVFoundation.h>

#import "st_mobile_face.h"
#import "CanvasView.h"

#ifndef CHECK_FLAG
#define CHECK_FLAG(action,flag) (((action)&(flag)) == flag)
#endif


@interface TrackingViewController () <AVCaptureVideoDataOutputSampleBufferDelegate>
{
    CGFloat _imageOnPreviewScale;
    CGFloat _previewImageWidth;
    CGFloat _previewImageHeight;
    
    st_handle_t _hTracker;
}

@property (nonatomic , strong) AVCaptureVideoPreviewLayer *captureVideoPreviewLayer;
@property (nonatomic , strong) AVCaptureDevice *device;
@property (nonatomic , strong) CanvasView *viewCanvas;
@property (nonatomic,strong) UILabel *poseResultLabel;


@end

@implementation TrackingViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
    
    self.view.backgroundColor = [UIColor blackColor];
    
    if (![self checkActiveCode]) {
        
        return;
    }
    
    NSString *strModelPath = [[NSBundle mainBundle] pathForResource:@"face_track_2.0.0" ofType:@"model"];
    st_result_t iRet = st_mobile_tracker_106_create(strModelPath.UTF8String,
                                                    ST_MOBILE_TRACKING_DEFAULT_CONFIG |
                                                    ST_MOBILE_TRACKING_ENABLE_DEBOUNCE |
                                                    ST_MOBILE_TRACKING_ENABLE_FACE_ACTION ,
                                                    &_hTracker);
    
    if (ST_OK != iRet || !_hTracker) {
        
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"错误提示" message:@"算法SDK初始化失败" delegate:nil cancelButtonTitle:@"好的" otherButtonTitles:nil, nil];
        [alert show];
        
        return;
    }
    
    // set what actions we want to get
    // if not call this function, the default setting will be used: get all face actions
    iRet = st_mobile_tracker_106_set_detect_actions(_hTracker,
                                                  ST_MOBILE_BROW_JUMP |
                                                  ST_MOBILE_EYE_BLINK |
                                                  ST_MOBILE_MOUTH_AH |
                                                  ST_MOBILE_HEAD_YAW |
                                                  ST_MOBILE_HEAD_PITCH);
    
    if (ST_OK != iRet) {
        
        NSLog(@"st_mobile_tracker_106_set_face_actions failed %d" , iRet);
        
        return;
    }
    
    //  st_mobile_tracker_106_set_facelimit(_hTracker, 1);
    //  st_mobile_tracker_106_set_detect_interval(_hTracker,60);
    
    [self setupAVCapture];
}

- (void)dealloc
{
    st_mobile_tracker_106_destroy(_hTracker);
}


- (NSString *)getSHA1StringWithData:(NSData *)data
{
    uint8_t digest[CC_SHA1_DIGEST_LENGTH];
    
    CC_SHA1(data.bytes, (unsigned int)data.length, digest);
    
    NSMutableString *strSHA1 = [NSMutableString string];
    
    for (int i = 0 ; i < CC_SHA1_DIGEST_LENGTH ; i ++) {
        
        [strSHA1 appendFormat:@"%02x" , digest[i]];
    }
    
    return strSHA1;
}

- (BOOL)checkActiveCode
{
    
    NSString *strLicensePath = [[NSBundle mainBundle] pathForResource:@"SENSEME_106" ofType:@"lic"];
    NSData *dataLicense = [NSData dataWithContentsOfFile:strLicensePath];
    
    NSString *strKeySHA1 = @"SENSEME_106";
    NSString *strKeyActiveCode = @"ACTIVE_CODE";
    
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    
    NSString *strStoredSHA1 = [userDefaults objectForKey:strKeySHA1];
    NSString *strLicenseSHA1 = [self getSHA1StringWithData:dataLicense];
    
    st_result_t iRet = ST_OK;
    
    // new or update
    if (!strStoredSHA1 || ![strLicenseSHA1 isEqualToString:strStoredSHA1]) {
        
        char active_code[1024];
        int active_code_len = 1024;
        
        // generate one
        st_result_t iRet = st_mobile_generate_activecode(strLicensePath.UTF8String, active_code, &active_code_len);
        
        if (ST_OK != iRet) {
            
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"错误提示" message:@"使用新的license文件生成激活码时失败，可能是授权文件过期。" delegate:nil cancelButtonTitle:@"好的" otherButtonTitles:nil, nil];
            
            [alert show];
            
            return NO;
            
        } else {
            
            // Store active code
            NSData *activeCodeData = [NSData dataWithBytes:active_code length:active_code_len];
            
            [userDefaults setObject:activeCodeData forKey:strKeyActiveCode];
            [userDefaults setObject:strLicenseSHA1 forKey:strKeySHA1];
            
            [userDefaults synchronize];
        }
    }else{
        
        // Get current active code
        // In this app active code was stored in NSUserDefaults
        // It also can be stored in other places
        NSData *activeCodeData = [userDefaults objectForKey:strKeyActiveCode];
        
        // Check if current active code is available
        iRet = st_mobile_check_activecode(strLicensePath.UTF8String, (const char *)[activeCodeData bytes]);
        
        if (ST_OK != iRet) {
            
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"错误提示" message:@"激活码无效" delegate:nil cancelButtonTitle:@"好的" otherButtonTitles:nil, nil];
            
            [alert show];
            
            return NO;
        }
    }
    
    return YES;
}

- (void)setupAVCapture
{
    AVCaptureSession *session = [[AVCaptureSession alloc] init];
    // Set the camera preview size
    session.sessionPreset = AVCaptureSessionPreset640x480;
    CGFloat imageWidth = 480;
    CGFloat imageHeight = 640;
    
    // Get the preview frame size.
    self.captureVideoPreviewLayer = [[AVCaptureVideoPreviewLayer alloc] initWithSession:session];
    self.captureVideoPreviewLayer.frame = self.view.bounds;
    CGFloat previewWidth = self.captureVideoPreviewLayer.frame.size.width;
    CGFloat previewHeight = self.captureVideoPreviewLayer.frame.size.height;
    [self.captureVideoPreviewLayer setVideoGravity:AVLayerVideoGravityResizeAspectFill];
    [self.view.layer addSublayer:self.captureVideoPreviewLayer];
    
    // Calculate the width, height and scale rate to display the preview image
    _imageOnPreviewScale = MAX(previewHeight/imageHeight, previewWidth/imageWidth);
    _previewImageWidth = imageWidth * _imageOnPreviewScale;
    _previewImageHeight = imageHeight * _imageOnPreviewScale;
    
    self.viewCanvas = [[CanvasView alloc] initWithFrame:CGRectMake(0., 0., _previewImageWidth, _previewImageHeight)];
    self.viewCanvas.center = self.view.center;
    self.viewCanvas.backgroundColor = [UIColor clearColor];
    [self.view addSubview:self.viewCanvas];
    
    self.poseResultLabel = [[UILabel alloc]initWithFrame:CGRectMake(15, 30, self.view.frame.size.width - 30, 75)];
    self.poseResultLabel.numberOfLines = 0;
    self.poseResultLabel.textAlignment = NSTextAlignmentCenter;
    self.poseResultLabel.backgroundColor = [UIColor redColor];
    self.poseResultLabel.font = [UIFont systemFontOfSize:14.0f];
    [self.poseResultLabel setAdjustsFontSizeToFitWidth:YES];
    [self.view addSubview:self.poseResultLabel];
    
    AVCaptureDevice *deviceFront;
    
    NSArray *devices = [AVCaptureDevice devices];
    for (AVCaptureDevice *device in devices) {
        
        if ([device hasMediaType:AVMediaTypeVideo]) {
            
            if ([device position] == AVCaptureDevicePositionFront) {
                deviceFront = device;
            }
        }
    }
    self.device = deviceFront;
    NSError *error = nil;
    AVCaptureDeviceInput *input = [AVCaptureDeviceInput deviceInputWithDevice:deviceFront error:&error];
    
    if (!input) {
        // Handle the error appropriately.
        NSLog(@"ERROR: trying to open camera: %@", error);
    }
    AVCaptureVideoDataOutput * dataOutput = [[AVCaptureVideoDataOutput alloc] init];
    
    [dataOutput setAlwaysDiscardsLateVideoFrames:YES];
    
    [dataOutput setVideoSettings:@{(id)kCVPixelBufferPixelFormatTypeKey: @(kCVPixelFormatType_420YpCbCr8BiPlanarFullRange)}];
    dispatch_queue_t queue = dispatch_queue_create("dataOutputQueue", NULL);
    [dataOutput setSampleBufferDelegate:self queue:queue];
    
    [session beginConfiguration];
    
    if ([session canAddInput:input]) {
        [session addInput:input];
    }
    if ([session canAddOutput:dataOutput]) {
        [session addOutput:dataOutput];
    }
    [session commitConfiguration];
    
    [session startRunning];
}

- (void)captureOutput:(AVCaptureOutput *)captureOutput didOutputSampleBuffer:(CMSampleBufferRef)sampleBuffer fromConnection:(AVCaptureConnection *)connection {
    
    CVPixelBufferRef pixelBuffer = (CVPixelBufferRef)CMSampleBufferGetImageBuffer(sampleBuffer);
    CVPixelBufferLockBaseAddress(pixelBuffer, 0);
    
    /**  如果输入的图像格式是BGRA 输入的宽度为   ibytesPerRow / 4
     For BGRA(aka kCVPixelFormatType_32BGRA)
     
     uint8_t *baseAddress = CVPixelBufferGetBaseAddress(pixelBuffer);
     int ibytesPerRow = (int)CVPixelBufferGetBytesPerRow(pixelBuffer);
     int iHeight = (int)CVPixelBufferGetHeight(pixelBuffer);
     int iWidth = ibytesPerRow/4;
     
     如果输入的图像格式是NV12 输入的宽度为   ibytesPerRow
     For NV12(aka kCVPixelFormatType_420YpCbCr8BiPlanarFullRange)
     compat with kCVPixelFormatType_420YpCbCr8BiPlanarVideoRange with loss of accuracy
     **/
    
    uint8_t *baseAddress = CVPixelBufferGetBaseAddressOfPlane(pixelBuffer, 0);
    int iBytesPerRow =(int)CVPixelBufferGetBytesPerRowOfPlane(pixelBuffer, 0);
    int iHeight = (int)CVPixelBufferGetHeightOfPlane(pixelBuffer, 0);
    int iWidth = (int)CVPixelBufferGetWidthOfPlane(pixelBuffer, 0);
    
    size_t iTop , iBottom , iLeft , iRight;
    CVPixelBufferGetExtendedPixels(pixelBuffer, &iLeft, &iRight, &iTop, &iBottom);
    
    iWidth = iWidth + (int)iLeft + (int)iRight;
    iHeight = iHeight + (int)iTop + (int)iBottom;

    st_mobile_face_action_t *pFaceAction = NULL;
    int iFaceCount = 0;
    st_result_t iRet = ST_OK;
    
    UIDeviceOrientation iDeviceOrientation = [[UIDevice currentDevice]orientation];
    
    BOOL isMirror = self.device.position == AVCaptureDevicePositionFront;
    
    st_rotate_type stMobileRotate;
    
    switch (iDeviceOrientation) {
            
        case UIDeviceOrientationPortrait:
            
            stMobileRotate = ST_CLOCKWISE_ROTATE_90;
            break;
            
        case UIDeviceOrientationPortraitUpsideDown:
            
            stMobileRotate = ST_CLOCKWISE_ROTATE_270;
            break;
            
        case UIDeviceOrientationLandscapeLeft:
            
            stMobileRotate = isMirror ? ST_CLOCKWISE_ROTATE_180 : ST_CLOCKWISE_ROTATE_0;
            break;
            
        case UIDeviceOrientationLandscapeRight:
            
            stMobileRotate = isMirror ? ST_CLOCKWISE_ROTATE_0 : ST_CLOCKWISE_ROTATE_180;
            break;
            
        default:
            
            stMobileRotate = ST_CLOCKWISE_ROTATE_90;
            break;
    }
    
    iRet = st_mobile_tracker_106_track_face_action(_hTracker, baseAddress, ST_PIX_FMT_NV12, iWidth, iHeight, iBytesPerRow, stMobileRotate, &pFaceAction, &iFaceCount);
    
    if (ST_OK != iRet) {
        
        NSLog(@"st_mobile_tracker_106_track_face_action failed. %d" , iRet);
        
        CVPixelBufferUnlockBaseAddress(pixelBuffer, 0);
        
        return;
    }
    
    if (iFaceCount > 0) {
        
        NSMutableArray *arrPersons = [NSMutableArray array];
        
        for (int i = 0; i < iFaceCount; i ++) {
            
            st_mobile_106_t stFace = pFaceAction[i].face;
            
            printf("ID : %d , eye_dist : %f , roll : %f , pitch : %f , yaw : %f , score : %f\n" ,stFace.ID ,stFace.eye_dist ,stFace.roll ,stFace.pitch ,stFace.yaw ,stFace.score);
            
            NSMutableArray *arrStrPoints = [NSMutableArray array];
            CGRect rectFace = CGRectZero;
            
            st_pointf_t *facialPoints = stFace.points_array;
            
            for(int i = 0; i < 106; i ++) {
                
                CGPoint point;
                if (isMirror) {
                    point.x = _imageOnPreviewScale * facialPoints[i].y;
                    point.y = _imageOnPreviewScale * facialPoints[i].x;
                } else {
                    point.x = _previewImageWidth - _imageOnPreviewScale * facialPoints[i].y;
                    point.y = _imageOnPreviewScale* facialPoints[i].x;
                }
                [arrStrPoints addObject:[NSValue valueWithCGPoint:point]];
            }
            
            st_rect_t rect = stFace.rect;
            
            if (isMirror) {
                rectFace = CGRectMake(_imageOnPreviewScale * rect.top,
                                      _imageOnPreviewScale * rect.left,
                                      _imageOnPreviewScale * rect.right - _imageOnPreviewScale * rect.left,
                                      _imageOnPreviewScale * rect.bottom - _imageOnPreviewScale * rect.top);
            } else {
                rectFace = CGRectMake(_previewImageWidth - _imageOnPreviewScale * rect.top - (_imageOnPreviewScale * rect.right - _imageOnPreviewScale * rect.left) ,
                                      _imageOnPreviewScale * rect.left,
                                      _imageOnPreviewScale * rect.right - _imageOnPreviewScale * rect.left,
                                      _imageOnPreviewScale * rect.bottom - _imageOnPreviewScale * rect.top);
            }
            
            NSMutableDictionary *dicPerson = [NSMutableDictionary dictionary];
            [dicPerson setObject:arrStrPoints forKey:POINTS_KEY];
            [dicPerson setObject:[NSValue valueWithCGRect:rectFace] forKey:RECT_KEY];
            
            [arrPersons addObject:dicPerson];
            
        }
        
        st_mobile_106_t stFace = pFaceAction[iFaceCount - 1].face;
        unsigned int iFaceAction = pFaceAction[iFaceCount - 1].face_action;
        
        NSString *strLastFacePose = [NSString stringWithFormat:@"pose : yaw=%f , pitch=%f\nroll=%f , eye_dist=%f", stFace.yaw , stFace.pitch , stFace.roll , stFace.eye_dist];
        NSString *strLastFaceAction = [NSString stringWithFormat:@"action : brow:%d , eye:%d\n mouth:%d , yaw:%d , pitch:%d" ,
                                       CHECK_FLAG(iFaceAction, ST_MOBILE_BROW_JUMP),
                                       CHECK_FLAG(iFaceAction, ST_MOBILE_EYE_BLINK),
                                       CHECK_FLAG(iFaceAction, ST_MOBILE_MOUTH_AH),
                                       CHECK_FLAG(iFaceAction, ST_MOBILE_HEAD_YAW),
                                       CHECK_FLAG(iFaceAction, ST_MOBILE_HEAD_PITCH)];
        
        dispatch_async(dispatch_get_main_queue(), ^{
            
            self.poseResultLabel.hidden = NO;
            
            self.poseResultLabel.text = [NSString stringWithFormat:@"%@\n%@" ,
                                         strLastFacePose ,
                                         strLastFaceAction];
            
            [self showFaceLandmarksAndFaceRectWithPersonsArray:arrPersons];
        } );
        
    } else {
        dispatch_async(dispatch_get_main_queue(), ^{
            self.poseResultLabel.hidden = YES;
            self.poseResultLabel.text = @"";
            [self hideFace];
        } );
    }
    
    CVPixelBufferUnlockBaseAddress(pixelBuffer, 0);
}


- (void)showFaceLandmarksAndFaceRectWithPersonsArray:(NSMutableArray *)arrPersons
{
    if (self.viewCanvas.hidden) {
        self.viewCanvas.hidden = NO;
    }
    self.viewCanvas.arrPersons = arrPersons;
    [self.viewCanvas setNeedsDisplay];
}

- (void)hideFace {
    if (!self.viewCanvas.hidden) {
        self.viewCanvas.hidden = YES;
    }
}

- (void)touchesBegan:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event
{
    [self.navigationController setNavigationBarHidden:![self.navigationController.navigationBar isHidden] animated:YES];
    [super touchesBegan:touches withEvent:event];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
