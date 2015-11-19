//
//  ViewController.swift
//  devdactic-rest
//
//  Created by Simon Reimler on 16/03/15.
//  Copyright (c) 2015 Devdactic. All rights reserved.
//

import UIKit
import SwiftyJSON

class MenuVC: UIViewController, UITableViewDataSource, UITableViewDelegate {
    
    var refreshControl: UIRefreshControl!
    var tableView:UITableView?
    var items = NSMutableArray()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.tableView?.delegate = self
        self.tableView?.dataSource = self
        
        refreshControl = UIRefreshControl()
        self.tableView?.addSubview(self.refreshControl)

        // Do any additional setup after loading the view, typically from a nib.
    }
    
    override func viewWillAppear(animated: Bool) {
        
        let frame:CGRect = CGRect(x: 0, y: 100, width: self.view.frame.width, height: self.view.frame.height-100)
        self.tableView = UITableView(frame: frame)
        self.tableView?.dataSource = self
        self.tableView?.delegate = self
        
        self.view.addSubview(self.tableView!)
        
        let btnData = UIButton(frame: CGRect(x: self.view.frame.width / 2, y: 25, width: self.view.frame.width / 2, height: 20))
        btnData.backgroundColor = UIColor.blackColor()
        btnData.setTitle("Profile", forState: UIControlState.Normal)
        btnData.addTarget(self, action: "addData", forControlEvents: UIControlEvents.TouchUpInside)
        
        let btnNav = UIButton(frame: CGRect(x: 0, y: 25, width: self.view.frame.width / 2, height: 20))
        btnNav.backgroundColor = UIColor.blackColor()
        btnNav.setTitle("Back", forState: UIControlState.Normal)
        btnNav.addTarget(self, action: "navigateBack", forControlEvents: UIControlEvents.TouchUpInside)
        
        self.view.addSubview(btnData)
        self.view.addSubview(btnNav)
    }
    
    
    func navigateBack() {

        self.dismissViewControllerAnimated(true, completion: nil)
        
    }
    
    func addData() {
        
        RestApiManager.sharedInstance.getRandomUser {
            (json: JSON, error: NSError?) in
            
            // Get the appropiate part of the JSON object (then iterate over it), or just get the whole if it is of one level
                let users: AnyObject = json["user"].object
                self.items.addObject(users)
                dispatch_async(dispatch_get_main_queue(), { self.tableView?.reloadData()})
        }
        
        //TODO: save data to coredata
        
    }
    
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.items.count;
    }
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        var cell = tableView.dequeueReusableCellWithIdentifier("CELL") as UITableViewCell?
        
       // if cell == nil {
            cell = UITableViewCell(style: UITableViewCellStyle.Value1, reuseIdentifier: "CELL")
       // }
        
        let user:JSON =  JSON(self.items[indexPath.row])
        
       // let picURL = user["picture"]["medium"].string
       // let url = NSURL(string: picURL!)
       // let data = NSData(contentsOfURL: url!) //make sure your image in this url does exist, otherwise unwrap in a if let check
        
        cell!.textLabel?.text = user.string
       // cell!.imageView?.image = UIImage(data: data!)
        
        return cell!
    }
    
}

