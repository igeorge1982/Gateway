//
//  Downloader.swift
//  MyFirstSwiftApp
//
//  Created by Gaspar Gyorgy on 18/07/15.
//  Copyright (c) 2015 Gaspar Gyorgy. All rights reserved.
//

import Foundation
import SwiftyJSON

typealias ServiceResponses = (JSON, NSError?) -> Void

class RequestManager: NSObject, NSURLSessionDelegate, NSURLSessionTaskDelegate {
    
//    var url: NSURL

 //   init(_ url: String) {
 //       self.url = NSURL(string: url)!
 //   }
    
    var url: NSURL!
    var errors: String!
    
    
    init?(url: String, errors: String) {
        super.init()
        self.url = NSURL(string: url)!
        self.errors = errors
        if url.isEmpty { }
        if errors.isEmpty { }
    }

   // lazy var config = NSURLSessionConfiguration.defaultSessionConfiguration()
   // lazy var session: NSURLSession = NSURLSession(configuration: self.config, delegate: self, delegateQueue:NSOperationQueue.mainQueue())
    
   lazy var session: NSURLSession = NSURLSession.sharedCustomSession

    var running = false
    
    func getResponse(onCompletion: (JSON, NSError?) -> Void) {
        
        dataTask ({ json, err in
        
            onCompletion(json as JSON, err)
        
        })
    }
    
    func dataTask(onCompletion: ServiceResponses) {
        
        let cachePolicy = NSURLRequestCachePolicy.ReloadIgnoringLocalCacheData
        let request:NSMutableURLRequest = NSMutableURLRequest(URL: url, cachePolicy: cachePolicy, timeoutInterval: 10)

        
        let task = session.dataTaskWithRequest(request, completionHandler: {data, response, sessionError -> Void in
            
          //  if  let json:JSON = try! JSON(data: data!) {
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

                let prefs:NSUserDefaults = NSUserDefaults.standardUserDefaults()
                let alertView:UIAlertView = UIAlertView()

                    NSLog("got a 200")
                    
                    if let user = json["user"].string {
                   
                        if !user.isEmpty {
                        
                        prefs.setObject(user, forKey: "USERNAME")
                        
                        alertView.title = "Welcome"
                        alertView.message = user as String
                        alertView.delegate = self
                        alertView.addButtonWithTitle("OK")
                        alertView.show()
                            
                            NSLog("User ==> %@", user);

                        
                        } else {
                           
                            alertView.title = "Sorry!"
                            alertView.message = "User does not exist!"
                            alertView.delegate = self
                            alertView.addButtonWithTitle("OK")
                            alertView.show()
                            
                            NSLog("User ==> %@", user);

                        }

                    } else {
                        
                        alertView.title = "Hmmm..."
                        alertView.message = "Something went wrong..." as String
                        alertView.delegate = self
                        alertView.addButtonWithTitle("OK")
                        alertView.show()
                        
                        NSLog("Dictionary\(["user"]) does not exist")
                    }
                
            self.running = false
            
            onCompletion(json, error)
                }
          //  }
        })
        
        running = true
        task.resume()
        
    }
        
    
    func URLSession(session: NSURLSession, didReceiveChallenge challenge: NSURLAuthenticationChallenge, completionHandler:      (NSURLSessionAuthChallengeDisposition, NSURLCredential?) -> Void) {
            
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
