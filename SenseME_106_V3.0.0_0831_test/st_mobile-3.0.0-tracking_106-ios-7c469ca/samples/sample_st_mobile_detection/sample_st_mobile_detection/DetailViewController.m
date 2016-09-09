//
//  DetailViewController.m
//  sample_face_detect
//
//  Created by sluin on 15/7/2.
//  Copyright (c) 2015年 SunLin. All rights reserved.
//

#import "DetailViewController.h"
#import <CommonCrypto/CommonDigest.h>

#import "st_mobile_face.h"

@interface DetailViewController ()

@property (nonatomic , strong) UIImageView *imageView;

@end

@implementation DetailViewController

- (void)viewDidLoad {
    [super viewDidLoad];

    if (![self checkActiveCode]) {
        
        return;
    }
    
    self.imageView = [[UIImageView alloc] initWithFrame:self.view.bounds];
    [self.imageView setImage:self.imageDetect];
    [self.imageView setBackgroundColor:[UIColor blackColor]];
    [self.view addSubview:self.imageView];
    
    int iWidth = self.imageDetect.size.width;
    int iHeight= self.imageDetect.size.height;
    
    float fScale = fmaxf(iWidth / CGRectGetWidth(self.imageView.frame), iHeight / CGRectGetHeight(self.imageView.frame));
    
    CGFloat fWidth = iWidth / fScale;
    CGFloat fHeight = iHeight / fScale;
    
    NSString *strModelPath = [[NSBundle mainBundle] pathForResource:@"face_track_2.0.0" ofType:@"model"];
    st_handle_t hDetector = NULL;
    
    st_result_t iRet = st_mobile_face_detection_create([strModelPath UTF8String], ST_MOBILE_DETECT_DEFAULT_CONFIG, &hDetector);
    
    if (ST_OK != iRet || !hDetector) {
        
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"错误提示" message:@"算法SDK初始化失败" delegate:nil cancelButtonTitle:@"好的" otherButtonTitles:nil, nil];
        [alert show];
        return;
    }
    
    
    unsigned char* pImage = (unsigned char *)malloc(sizeof(unsigned char) * iWidth * iHeight * 4);
    
    [self convertUIImage:self.imageDetect toBGRABytes:pImage];
    
    st_mobile_106_t *pFaceArray = NULL;
    int iFaceCount = 0;
    double dStartTime = CFAbsoluteTimeGetCurrent();
    iRet = st_mobile_face_detection_detect(hDetector, pImage, ST_PIX_FMT_BGRA8888, iWidth, iHeight, iWidth * 4, ST_CLOCKWISE_ROTATE_0, &pFaceArray, &iFaceCount);
    printf("cost %f ms\n" , (CFAbsoluteTimeGetCurrent() - dStartTime) * 1000.0);
    
    
    if (iRet != ST_OK) {
        
        NSLog(@"detection failed.");
    }else{
        
        UIGraphicsBeginImageContext(self.imageView.frame.size);
        [self.imageDetect drawInRect:CGRectMake(0, 0, fWidth, fHeight)];
        CGContextSetLineWidth(UIGraphicsGetCurrentContext(), 2.0);
        
        for (int i = 0; i < iFaceCount; i ++ ) {
            
            st_mobile_106_t pFace = pFaceArray[i];
            
            st_pointf_t *pPoint = pFace.points_array;
            st_rect_t pRect = pFace.rect;
            
            float fWidth = (pRect.right - pRect.left) / fScale;
            
            CGRect rectFace = CGRectMake(pRect.left / fScale , pRect.top / fScale , fWidth , fWidth);
            
            CGContextAddRect(UIGraphicsGetCurrentContext(), rectFace);
            
            for (int j = 0; j < 106; j ++) {
                CGContextAddRect(UIGraphicsGetCurrentContext(), CGRectMake( pPoint[j].x / fScale , pPoint[j].y / fScale , 0.5 , 0.5));
            }
        }
        
        CGContextSetRGBStrokeColor(UIGraphicsGetCurrentContext(), 0.0, 1.0, 0.0, 1.0);
        CGContextStrokePath(UIGraphicsGetCurrentContext());
        self.imageView.image = UIGraphicsGetImageFromCurrentImageContext();
        UIGraphicsEndImageContext();
        
        st_mobile_face_detection_release_result(pFaceArray, iFaceCount);
    }
    
    st_mobile_face_detection_destroy(hDetector);
    
    free(pImage);
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

- (void)convertUIImage:(UIImage *)image toBGRABytes:(unsigned char *)pImage
{
    CGImageRef cgImage = [image CGImage];
    CGColorSpaceRef colorspace = CGColorSpaceCreateDeviceRGB();
    
    int iWidth = image.size.width;
    int iHeight = image.size.height;
    int iBytesPerPixel = 4;
    int iBytesPerRow = iBytesPerPixel * iWidth;
    int iBitsPerComponent = 8;
    
    CGContextRef context = CGBitmapContextCreate(pImage,
                                                 iWidth,
                                                 iHeight,
                                                 iBitsPerComponent,
                                                 iBytesPerRow,
                                                 colorspace,
                                                 kCGBitmapByteOrder32Little | kCGImageAlphaPremultipliedFirst
                                                 );
    
    if (!context) {
        CGColorSpaceRelease(colorspace);
        return;
    }
    CGRect rect = CGRectMake(0 , 0 , iWidth , iHeight);
    CGContextDrawImage(context , rect ,cgImage);
    CGColorSpaceRelease(colorspace);
    CGContextRelease(context);
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];

}


@end
