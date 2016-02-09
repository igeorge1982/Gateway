//
//  File.swift
//  SwiftLoginScreen
//
//  Created by Gaspar Gyorgy on 09/11/15.
//  Copyright © 2015 Dipin Krishna. All rights reserved.
//

import Foundation
import SwiftyJSON

typealias ServiceResponse = (JSON, NSError?) -> Void

class RestApiManager: NSObject, NSURLSessionDelegate, NSURLSessionTaskDelegate  {

    static let sharedInstance = RestApiManager()

    //let baseURL = "http://api.randomuser.me/"
    let baseURL = "https://milo.crabdance.com/login/admin?JSESSIONID="

    func getRandomUser(onCompletion: (JSON, NSError?) -> Void) {
        
        let prefs:NSUserDefaults = NSUserDefaults.standardUserDefaults()
        
        let sessionId:NSString = prefs.valueForKey("JSESSIONID") as! NSString
        
        let route = baseURL+(sessionId as String)
       // let route = baseURL
        
        makeHTTPGetRequest(route, onCompletion: { json, err in
            onCompletion(json as JSON, err)
        })
    }
    
    func makeHTTPGetRequest(path: String, onCompletion: ServiceResponse) {
        
        //let ciphertext = cipherText.getCipherText()
        
        let request = NSMutableURLRequest.requestWithURL(NSURL(string: path)!, method: "GET", queryParameters: nil, bodyParameters: nil, headers: ["Ciphertext":                             xtoken as! String])
        
       //let configuration = NSURLSessionConfiguration.defaultSessionConfiguration()
       //let session = NSURLSession(configuration: configuration, delegate: self, delegateQueue:NSOperationQueue.mainQueue())

        let session = NSURLSession.sharedCustomSession
        
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
                
                alertView.title = "Error!"
                alertView.message = "Connection Failure: \(error!.description)"
                alertView.delegate = self
                alertView.addButtonWithTitle("OK")
                alertView.show()
                
                
            } else {
            
            let json:JSON = JSON(data: data!)
                
                onCompletion(json, error)
            }
        })
        task.resume()
    }
    
    //MARK: Perform a POST Request
    func makeHTTPPostRequest(path: String, body: [String: AnyObject], onCompletion: ServiceResponse) {
        
        var err: NSError?
        let request = NSMutableURLRequest(URL: NSURL(string: path)!)
        
        // Set the method to POST
        request.HTTPMethod = "POST"

        let body = body as? NSData
        
        // Set the POST body for the request
        request.HTTPBody = (try! NSJSONSerialization.JSONObjectWithData(body!, options:NSJSONReadingOptions.MutableContainers)) as? NSData
        
        //NSJSONSerialization.dataWithJSONObject(body, options: nil, error: &err)
        let session = NSURLSession.sharedSession()
        
        let task = session.dataTaskWithRequest(request, completionHandler: {data, response, error -> Void in
            let json:JSON = JSON(data: data!)
            onCompletion(json, err)
        })
        task.resume()
    }
    
    func URLSession(session: NSURLSession, didReceiveChallenge challenge: NSURLAuthenticationChallenge, completionHandler:
        (NSURLSessionAuthChallengeDisposition, NSURLCredential?) -> Void) {
            
            print("didReceiveAuthenticationChallenge")
            
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