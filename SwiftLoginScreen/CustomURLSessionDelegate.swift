//
//  f.swift
//  SwiftLoginScreen
//
//  Created by Gaspar Gyorgy on 18/11/15.
//  Copyright © 2015 Dipin Krishna. All rights reserved.
//

import Foundation

class CustomURLSessionDelegate: NSObject, NSURLSessionDelegate {
    
    // MARK: - NSURLSessionDelegate
    
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
    }
    
}