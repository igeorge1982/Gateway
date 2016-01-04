//
//  MyURLProtocol.swift
//  NSURLProtocolExample
//
//  Created by Gaspar Gyorgy on 05/09/15.
//  Copyright (c) 2015 Zedenem. All rights reserved.
//

import UIKit
import SwiftyJSON
import CoreData
import Foundation
import Kanna


var requestCount = 0
var pattern_ = "https://([^/]+)(/example/tabularasa.jsp.*?)(/$|$)"
var pattern_rs = "https://([^/]+)(/example/tabularasa.jsp.*?JSESSIONID=)"

class MyURLProtocol: NSURLProtocol {

    var connection: NSURLConnection!
    var mutableData: NSMutableData!
    var response: NSURLResponse!
    var httpresponse: NSHTTPURLResponse!


    override class func canInitWithRequest(request: NSURLRequest) -> Bool {
    
        if NSURLProtocol.propertyForKey("MyURLProtocolHandledKey", inRequest: request) != nil {
            return false
        }
        
        if NSURLProtocol.propertyForKey("MyRedirectHandledKey", inRequest: request) != nil {
            return false
        }
        
        print("Request #\(requestCount++): URL = \(request.URL!.absoluteString)")
        NSLog("Relative path ==> %@", request.URL!.relativePath!)


        return true
        
    }
    
    override class func canonicalRequestForRequest(request: NSURLRequest) -> NSURLRequest {
        return request
    }
    
    override class func requestIsCacheEquivalent(aRequest: NSURLRequest,toRequest bRequest: NSURLRequest) -> Bool {
            return super.requestIsCacheEquivalent(aRequest, toRequest:bRequest)
    }
    
    override func startLoading() {

        if AFNetworkReachabilityManager.sharedManager().reachable {
            NSLog("AFNetwork is reachable...")
        
            // 1
        let possibleCachedResponse = self.cachedResponseForCurrentRequest()
        
        if let cachedResponse = possibleCachedResponse {
        print("Serving response from cache")
            // 2
            let data = cachedResponse.valueForKey("data") as! NSData
            let mimeType = cachedResponse.valueForKey("mimeType") as! String
            let encoding = cachedResponse.valueForKey("encoding") as? String?
            
            // 3
            let response = NSURLResponse(URL: self.request.URL!, MIMEType: mimeType, expectedContentLength: data.length, textEncodingName: encoding!)
            
            // 4
            self.client!.URLProtocol(self, didReceiveResponse: response, cacheStoragePolicy: .NotAllowed)
            self.client!.URLProtocol(self, didLoadData: data)
            self.client!.URLProtocolDidFinishLoading(self)
            
            
        
        } else {
            
            // 5
            print("Serving response from NSURLConnection")
            
            if request.URL!.relativePath != "/example/tabularasa.jsp" {
             
            let newRequest = self.request.mutableCopy() as! NSMutableURLRequest
            NSURLProtocol.setProperty(true, forKey: "MyURLProtocolHandledKey", inRequest: newRequest)
            newRequest.setValue("M", forHTTPHeaderField: "M")
            self.connection = NSURLConnection(request: newRequest, delegate: self)
            
                }
                        
            }
        
        } else {
            
            NSLog("AFNetwork failed to respond......")
            
            let failedResponse = NSHTTPURLResponse(URL: self.request.URL!, statusCode: 0, HTTPVersion: nil, headerFields: nil)
            
            self.client?.URLProtocol(self, didReceiveResponse: failedResponse!, cacheStoragePolicy: .NotAllowed)
            
            self.client?.URLProtocolDidFinishLoading(self)
            
        }
    }
    
    override func stopLoading() {
        if self.connection != nil {
            self.connection.cancel()
        }
        self.connection = nil
    }
    
    
    func connection(connection: NSURLConnection!, willSendRequest request: NSURLRequest, redirectResponse response: NSURLResponse?) -> NSURLRequest? {

        if let httpResponse = response as? NSHTTPURLResponse {
        
        if httpResponse.statusCode >= 300 && httpResponse.statusCode < 400
            {
                let newRequest = self.request.mutableCopy() as! NSMutableURLRequest
                NSURLProtocol.setProperty(true, forKey: "MyRedirectHandledKey", inRequest: newRequest)
           //   self.client?.URLProtocol(self, wasRedirectedToRequest: newRequest, redirectResponse: response!)
                NSLog("Sending Request from %@ to %@", response!.URL!, request.URL!);
                
                
                let match = RegEx()
                let url = request.URL!.absoluteString
                var requestLogin:RequestManager?
                
                if match.containsMatch(pattern_, inString: url) {
                    
                    let adminUrl = match.replaceMatches(pattern_rs, inString: url, withString:"https://milo.crabdance.com/login/admin?JSESSIONID=")
                    let sessionID = match.replaceMatches(pattern_rs, inString: url, withString:"")
                    
                    let prefs:NSUserDefaults = NSUserDefaults.standardUserDefaults()
                    prefs.setValue(sessionID, forKey: "JSESSIONID")
                    NSLog("SessionId ==> %@", sessionID!)

                    requestLogin = RequestManager(url: adminUrl!, errors: "")
                    
                    requestLogin?.getResponse {
                        (resultString) -> Void in
                        
                        print(resultString)
                    }
                }
                
                
            }
        }
        
      //  self.client!.URLProtocol(self, wasRedirectedToRequest: request, redirectResponse: response!)
        NSLog("Url to be redirected ==> %@", request.URL!.absoluteString)
        return request
        
    }
    
    func connection(connection: NSURLConnection!, didReceiveResponse response: NSURLResponse!) {
        self.client!.URLProtocol(self, didReceiveResponse: response, cacheStoragePolicy: .NotAllowed)
        
        self.response = response
        self.mutableData = NSMutableData()
        self.httpresponse = response as? NSHTTPURLResponse!

        
    }
    
    func connection(connection: NSURLConnection!, didReceiveData data: NSData!) {
        
        self.client!.URLProtocol(self, didLoadData: data)
        self.mutableData.appendData(data)
        
    }
    
    func connectionDidFinishLoading(connection: NSURLConnection!) {
        self.client!.URLProtocolDidFinishLoading(self)
        self.saveCachedResponse()
    }
    
    func connection(connection: NSURLConnection!, didFailWithError error: NSError!) {
        self.client!.URLProtocol(self, didFailWithError: error)
    
    }
    
    func saveCachedResponse () {
        print("Saving cached response")
        
        // 1
        let delegate = UIApplication.sharedApplication().delegate as! AppDelegate
        let context = delegate.managedObjectContext
        
        // 2
        let cachedResponse = NSEntityDescription.insertNewObjectForEntityForName("CachedURLResponse", inManagedObjectContext: context) as NSManagedObject
        
        if let httpResponse = response as? NSHTTPURLResponse {
        
            switch(httpResponse.statusCode) {
            case 200:
               
                if request.URL!.relativePath != "/login/HelloWorld" {
                    
                cachedResponse.setValue(self.mutableData, forKey: "data")
                cachedResponse.setValue(self.request.URL!.absoluteString, forKey: "url")
                cachedResponse.setValue(NSDate(), forKey: "timestamp")
                cachedResponse.setValue(self.response.MIMEType, forKey: "mimeType")
                cachedResponse.setValue(self.response.textEncodingName, forKey: "encoding")
                cachedResponse.setValue(self.httpresponse.statusCode, forKey: "statusCode")
        
                }
                
            case 502:
                
                if request.URL!.relativePath == "/login/HelloWorld" {
                    let prefs:NSUserDefaults = NSUserDefaults.standardUserDefaults()
                    prefs.setInteger(0, forKey: "ISWEBLOGGEDIN")
                    
                    var errorOnLogin:RequestManager?
                    
                        let data: NSData = self.mutableData
                        
                        if let jsonData:NSDictionary = (try? NSJSONSerialization.JSONObjectWithData(data, options:NSJSONReadingOptions.MutableContainers )) as? Dictionary<String, AnyObject> {
                            
                            let errmsg = jsonData["Session creation"] as! String
                            errorOnLogin = RequestManager(url: "https://milo.crabdance.com/login/HelloWorld", errors: errmsg)
                            errorOnLogin?.getResponse { _ in }
                        
                    }


                }
                
            case 503:

                if request.URL!.relativePath == "/login/HelloWorld" {
                    let prefs:NSUserDefaults = NSUserDefaults.standardUserDefaults()
                    prefs.setInteger(0, forKey: "ISWEBLOGGEDIN")
                    
                    var errorOnLogin:RequestManager?
                    
                    let data: NSData = self.mutableData
                
                    if let result = (try? NSString(data: data, encoding: NSASCIIStringEncoding)) as? String {
                        
                        if let doc = Kanna.HTML(html: result, encoding: NSASCIIStringEncoding) {
                            errorOnLogin = RequestManager(url: "https://milo.crabdance.com/login/HelloWorld", errors: doc.title!)
                            errorOnLogin?.getResponse { _ in }
                            
                        }

                    }
    
                }
                
            default:
                
                NSLog("Url was redirected.")

            }
            
        }
    
        if context.hasChanges {
            do {
                try context.save()
            } catch {
                
                let nserror = error as NSError
                NSLog("Unresolved error \(nserror), \(nserror.userInfo)")
            }
        
        }
        
    }
    
    func cachedResponseForCurrentRequest() -> NSManagedObject? {
        // 1
        let delegate = UIApplication.sharedApplication().delegate as! AppDelegate
        let context = delegate.managedObjectContext
        
        // 2
        let fetchRequest = NSFetchRequest()
        let entity = NSEntityDescription.entityForName("CachedURLResponse", inManagedObjectContext: context)
        fetchRequest.entity = entity
        
        // 3
        let predicate = NSPredicate(format:"url == %@", self.request.URL!.absoluteString)
        fetchRequest.predicate = predicate
        
        
        // 4
        let possibleResult = try?context.executeFetchRequest(fetchRequest) as! Array<NSManagedObject>
        /*
        let fetchRequests = NSFetchRequest(entityName: "CachedURLResponse")
        let results = try?context.executeFetchRequest(fetchRequests) as! [CachedURLResponse]
        
        for managedObject in results! {
            if let url = managedObject.valueForKey("url"), statusCode = managedObject.valueForKey("statusCode") {
                print("\(url) \(statusCode)")
            }
        }*/
        
        // 5
        if let result = possibleResult {
            if !result.isEmpty {
              return result[0]
            
                }
            }
        
        return nil
    }

}