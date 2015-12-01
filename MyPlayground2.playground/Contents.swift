//: Playground - noun: a place where people can play

import Foundation
import XCPlayground


let baseURL = "https://milo.crabdance.com/login/admin"
/*
typealias CallbackBlock = (result: String, error: String?) -> ()

func httpGet(request: NSURLRequest!, callback: (String, String?) -> Void) {
    let session = NSURLSession.sharedSession()
    let task = session.dataTaskWithRequest(request){
        
        (data, response, error) -> Void in
        
        if error != nil {
            callback("", error!.localizedDescription)
       
        } else {
        
            let result = NSString(data: data!, encoding:
                NSASCIIStringEncoding)!
            callback(result as String, nil)
        }
    }
    task.resume()
}

var request = NSMutableURLRequest(URL: NSURL(string: baseURL)!)

httpGet(request){
    
    (data, error) -> Void in
    if error != nil {
        print(error)
    } else {
        print(data)
    }
}*/



class LearnNSURLSession: NSObject, NSURLSessionDelegate, NSURLSessionTaskDelegate {

    typealias CallbackBlock = (result: String, error: String?) -> ()


    var callback: CallbackBlock = {
        (resultString, error) -> Void in
        if error == nil {
            print(resultString)
        } else {
            print(error)
        }
    }

func httpGet(request: NSMutableURLRequest!, callback: (String, String?) -> Void) {

let configuration = NSURLSessionConfiguration.defaultSessionConfiguration()

let session = NSURLSession(configuration: configuration, delegate: self, delegateQueue:NSOperationQueue.mainQueue())

let task = session.dataTaskWithRequest(request){

    (data: NSData?, response: NSURLResponse?, error: NSError?) -> Void in
    if error != nil {
        callback("", error!.localizedDescription)

    } else {
        let result = NSString(data: data!, encoding: NSASCIIStringEncoding)!

            callback(result as String, nil)
            }
        }
    task.resume()
    }


func URLSession(session: NSURLSession, didReceiveChallenge challenge: NSURLAuthenticationChallenge, completionHandler:
(NSURLSessionAuthChallengeDisposition, NSURLCredential?) -> Void) {

    print("didReceiveAuthenticationChallenge")

completionHandler(

    NSURLSessionAuthChallengeDisposition.UseCredential,
    NSURLCredential(forTrust: challenge.protectionSpace.serverTrust!))
}
  
    /*
func URLSession(session: NSURLSession, didReceiveChallenge challenge: NSURLAuthenticationChallenge, completionHandler: (NSURLSessionAuthChallengeDisposition, NSURLCredential?) -> Void) {
            
            // For example, you may want to override this to accept some self-signed certs here.
            if challenge.protectionSpace.authenticationMethod == NSURLAuthenticationMethodServerTrust &&
                Constants.selfSignedHosts.contains(challenge.protectionSpace.host) {
                    
                    // Allow the self-signed cert.
                    let credential = NSURLCredential(forTrust: challenge.protectionSpace.serverTrust!)
                    completionHandler(.UseCredential, credential)
            } else {
                // You *have* to call completionHandler either way, so call it to do the default action.
                completionHandler(.PerformDefaultHandling, nil)
            }
    }
    
    // MARK: - Constants
    
    struct Constants {
        
        // A list of hosts you allow self-signed certificates on.
        // You'd likely have your dev/test servers here.
        // Please don't put your production server here!
        static let selfSignedHosts: Set<String> = ["milo.crabdance.com"]
    }*/

func URLSession(session: NSURLSession, task: NSURLSessionTask, willPerformHTTPRedirection response: NSHTTPURLResponse,
    newRequest request: NSURLRequest, completionHandler: (NSURLRequest?) -> Void) {

let newRequest : NSURLRequest? = request

    print(newRequest?.description);
    completionHandler(newRequest)
    }
}


var learn = LearnNSURLSession()

var request = NSMutableURLRequest(URL: NSURL(string: baseURL)!)

    learn.httpGet(request) {

        (resultString, error) -> Void in
        learn.callback(result: resultString, error: error)
        print(resultString)

    }


XCPSetExecutionShouldContinueIndefinitely(true)
