//
//  LoginVC.swift
//  SwiftLoginScreen
//
//  Created by Dipin Krishna on 31/07/14.
//  Copyright (c) 2014 Dipin Krishna. All rights reserved.
//

import UIKit
import SwiftyJSON


class LoginVC: UIViewController,UITextFieldDelegate, NSURLSessionDelegate, NSURLSessionTaskDelegate {
    
    var imageView:UIImageView = UIImageView()
    var backgroundDict:Dictionary<String, String> = Dictionary()
    
    //lazy var config = NSURLSessionConfiguration.defaultSessionConfiguration()
    //lazy var session: NSURLSession = NSURLSession(configuration: self.config, delegate: self, delegateQueue:NSOperationQueue.mainQueue())
    
    lazy var session = NSURLSession.sharedCustomSession

    
    var running = false
    
    @IBOutlet var txtUsername : UITextField!
    @IBOutlet var txtPassword : UITextField!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
        
        backgroundDict = ["Login":"login"]
        
        let view:UIView = UIView(frame: CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height));
        
        self.view.addSubview(view)
        
        self.view.sendSubviewToBack(view)
        
        
        let backgroundImage:UIImage? = UIImage(named: backgroundDict["Login"]!)
        
        
        imageView = UIImageView(frame: view.frame);
        
        imageView.image = backgroundImage;
        
        view.addSubview(imageView);
        
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    /*
    // #pragma mark - Navigation
    
    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue?, sender: AnyObject?) {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
    }
    */
    
    let url:NSURL = NSURL(string:"https://milo.crabdance.com/login/HelloWorld")!
    
    typealias ServiceResponse = (JSON, NSError?) -> Void
    
    func dataTask(username: String, hash: String, deviceId: String, systemVersion: String, onCompletion: ServiceResponse) {
        
        let request:NSMutableURLRequest = NSMutableURLRequest(URL: url)
        
        let post:NSString = "user=\(username)&pswrd=\(hash)&deviceId=\(deviceId)&ios=\(systemVersion)"
        
        NSLog("PostData: %@",post);
        
        let postData:NSData = post.dataUsingEncoding(NSASCIIStringEncoding)!
        
        let postLength:NSString = String( postData.length )
        
        request.HTTPMethod = "POST"
        request.HTTPBody = postData
        request.setValue(postLength as String, forHTTPHeaderField: "Content-Length")
        request.setValue("application/x-www-form-urlencoded", forHTTPHeaderField: "Content-Type")
        request.setValue("application/json", forHTTPHeaderField: "Accept")
        
        let task = session.dataTaskWithRequest(request, completionHandler: {data, response, sessionError -> Void in
            
            var error = sessionError
            
            if let httpResponse = response as? NSHTTPURLResponse {
                
                if httpResponse.statusCode < 200 || httpResponse.statusCode >= 300 {
                    
                    let description = "HTTP response was \(httpResponse.statusCode)"
                    
                    error = NSError(domain: "Custom", code: 0, userInfo: [NSLocalizedDescriptionKey: description])
                    NSLog(error!.description)
                    
                }
            }
            
            if error != nil {
                
                let alertView:UIAlertView = UIAlertView()
                
                alertView.title = "Sign in Failed!"
                alertView.message = "Connection Failure: \(error!.localizedDescription)"
                alertView.delegate = self
                alertView.addButtonWithTitle("OK")
                alertView.show()
                
                
            } else {
            
            let json:JSON = JSON(data: data!)
            
            if let httpResponse = response as? NSHTTPURLResponse {
                print("got some data")
                
                switch(httpResponse.statusCode) {
                case 200:
                    
                    do {
                        
                        let jsonData:NSDictionary = try NSJSONSerialization.JSONObjectWithData(data!, options:
                            
                            NSJSONReadingOptions.MutableContainers ) as! NSDictionary
                    
                        let success:NSInteger = jsonData.valueForKey("success") as! NSInteger
                        let sessionID:NSString = jsonData.valueForKey("JSESSIONID") as! NSString
                        
                        NSLog("sessionId ==> %@", sessionID);
                        
                        NSLog("Success: %ld", success);
                        
                        if(success == 1)
                        {
                            NSLog("Login SUCCESS");
                            
                            let prefs:NSUserDefaults = NSUserDefaults.standardUserDefaults()
                            
                            prefs.setObject(username, forKey: "USERNAME")
                            prefs.setInteger(1, forKey: "ISLOGGEDIN")
                            prefs.setValue(sessionID, forKey: "JSESSIONID")
                            prefs.setValue(deviceId, forKey: "deviceId")
                            
                            print(prefs)
                            prefs.synchronize()
                        }
                        
                    NSLog("got a 200")
                    self.dismissViewControllerAnimated(true, completion: nil)
                        
                    } catch {
                        
                        //TODO: handle error
                    }
                    
                default:
                    
                    let alertView:UIAlertView = UIAlertView()
                    alertView.title = "Sign in Failed!"
                    alertView.message = "Server error \(httpResponse.statusCode)"
                    alertView.delegate = self
                    alertView.addButtonWithTitle("OK")
                    alertView.show()
                    NSLog("Got an HTTP \(httpResponse.statusCode)")
                }
                
            } else {
                
                let alertView:UIAlertView = UIAlertView()
                
                alertView.title = "Sign in Failed!"
                alertView.message = "Connection Failure"
                
                alertView.delegate = self
                alertView.addButtonWithTitle("OK")
                alertView.show()
                NSLog("Connection Failure")
            }
            
            self.running = false
            onCompletion(json, error)
            
            }
        })
        
        running = true
        task.resume()
        
    }
    
    
    
    @IBAction func signinTapped(sender : UIButton) {
        let deviceId = UIDevice.currentDevice().identifierForVendor!.UUIDString
        let username:NSString = txtUsername.text!
        let password:NSString = txtPassword.text!
        let systemVersion = UIDevice.currentDevice().systemVersion

        //let AES = CryptoJS.AES()
        let SHA3 = CryptoJS.SHA3()

        //let encrypted = AES.encrypt(password as String, secretKey: "password123")
        //let decrypted = AES.decrypt(encrypted, secretKey: "password123")
        let hash = SHA3.hash(password as String,outputLength: 512)
        
        
        if ( username.isEqualToString("") || password.isEqualToString("") ) {
            
            let alertView:UIAlertView = UIAlertView()
            alertView.title = "Sign in Failed!"
            alertView.message = "Please enter Username and Password"
            alertView.delegate = self
            alertView.addButtonWithTitle("OK")
            alertView.show()
        
        } else {
            
            self.dataTask(username as String, hash: hash, deviceId: deviceId, systemVersion: systemVersion){
                (resultString, error) -> Void in
                
                print(resultString)
                
            }
                    
    }
    
   
    
    func textFieldShouldReturn(textField: UITextField) -> Bool {
       
            textField.resignFirstResponder()
            return true
        }
    
    }
    
    func URLSession(session: NSURLSession, didReceiveChallenge challenge: NSURLAuthenticationChallenge, completionHandler:
        (NSURLSessionAuthChallengeDisposition, NSURLCredential?) -> Void) {
            
            completionHandler(
                
                NSURLSessionAuthChallengeDisposition.UseCredential,
                NSURLCredential(forTrust: challenge.protectionSpace.serverTrust!))
    }
    
    func URLSession(session: NSURLSession, task: NSURLSessionTask, willPerformHTTPRedirection response: NSHTTPURLResponse,
        newRequest request: NSURLRequest, completionHandler: (NSURLRequest?) -> Void) {
            
            let newRequest : NSURLRequest? = request
            
            print(newRequest?.description);
            completionHandler(newRequest)
    }

}
