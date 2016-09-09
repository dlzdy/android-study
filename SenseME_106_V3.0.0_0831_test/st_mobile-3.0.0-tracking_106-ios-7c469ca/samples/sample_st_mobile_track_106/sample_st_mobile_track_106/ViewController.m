//
//  ViewController.m
//  sample_st_mobile_track_106
//
//  Created by sluin on 16/5/24.
//  Copyright © 2016年 SenseTime. All rights reserved.
//

#import "ViewController.h"
#import "TrackingViewController.h"

@interface ViewController ()

@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    [self.navigationController setNavigationBarHidden:YES animated:YES];
}

- (IBAction)onBtnStartTracking:(id)sender {
    
    TrackingViewController *trackingVC = [self.storyboard instantiateViewControllerWithIdentifier:@"TrackingViewController"];
    [self.navigationController pushViewController:trackingVC animated:YES];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
