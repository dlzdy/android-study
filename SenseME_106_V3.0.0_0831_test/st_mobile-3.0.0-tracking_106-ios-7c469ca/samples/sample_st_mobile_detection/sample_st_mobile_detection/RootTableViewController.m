//
//  RootTableViewController.m
//  sample_face_detect
//
//  Created by sluin on 15/7/2.
//  Copyright (c) 2015年 SunLin. All rights reserved.
//

#import "RootTableViewController.h"
#import "DetailViewController.h"

@interface RootTableViewController () <UINavigationControllerDelegate , UIImagePickerControllerDelegate>

@property (nonatomic , strong) NSMutableArray *arrDataSource;

@end

@implementation RootTableViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.title = @"sensetime";
    
    self.clearsSelectionOnViewWillAppear = YES;
    
    UIBarButtonItem *rightItem = [[UIBarButtonItem alloc] initWithTitle:@"相册" style:UIBarButtonItemStyleBordered target:self action:@selector(onAlbum)];
    self.navigationItem.rightBarButtonItem = rightItem;
    
    self.arrDataSource = [NSMutableArray array];
    
    for (int i = 0; i < 6; i ++) {
        [self.arrDataSource addObject:[NSString stringWithFormat:@"face%d.jpg",i]];
    }
    
    [self.tableView reloadData];
}

-(void)onAlbum
{
    if ([UIImagePickerController isSourceTypeAvailable:UIImagePickerControllerSourceTypePhotoLibrary | UIImagePickerControllerSourceTypeSavedPhotosAlbum])
    {
        UIImagePickerController *imagePicker = [[UIImagePickerController alloc] init];
        imagePicker.delegate = self;
        imagePicker.sourceType = UIImagePickerControllerSourceTypeSavedPhotosAlbum | UIImagePickerControllerSourceTypePhotoLibrary;
        imagePicker.allowsEditing = YES;
        [self.navigationController presentViewController:imagePicker animated:YES completion:nil];
    }else{
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"温馨提示" message:@"相册不可用, 请更改权限设置" delegate:nil cancelButtonTitle:@"ok" otherButtonTitles:nil, nil];
        [alert show];
    }
}


#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return self.arrDataSource.count;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"imageCell" forIndexPath:indexPath];
    
    NSString *strImageFileName = [self.arrDataSource objectAtIndex:indexPath.row];
    cell.imageView.image = [UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:strImageFileName ofType:@""]];
    cell.textLabel.text =  strImageFileName;
    
    return cell;
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    [self detectImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:[self.arrDataSource objectAtIndex:indexPath.row] ofType:@""]]];
}

#pragma mark - UIImagePickerControllerDelegate

-(void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info
{
    [self.navigationController dismissViewControllerAnimated:YES completion:^{
        [self detectImage:[info objectForKey:UIImagePickerControllerEditedImage]];
    }];
}

-(void)imagePickerControllerDidCancel:(UIImagePickerController *)picker
{
    [self.navigationController dismissViewControllerAnimated:YES completion:nil];
}

-(void)detectImage:(UIImage *)image
{
    DetailViewController *detailVC = [self.storyboard instantiateViewControllerWithIdentifier:@"DetailViewController"];
    
    detailVC.imageDetect = image;
    
    [self.navigationController pushViewController:detailVC animated:YES];
}




- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


@end
