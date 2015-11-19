//: Playground - noun: a place where people can play

import UIKit

var str = "Hello, playground"

//current date
let date = NSDate();
print("Date 1 \(date)")

var formatter = NSDateFormatter();
formatter.dateFormat = "yyyy-MM-dd HH:mm:ss ZZZ";
let defaultTimeZoneStr = formatter.stringFromDate(date);

// "2015-04-01 08:52:00 -0400" <-- same date, local, but with seconds
formatter.timeZone = NSTimeZone(abbreviation: "UTC");

let utcTimeZoneStr = formatter.stringFromDate(date)
print("Date 2 \(utcTimeZoneStr)")
// "2015-04-01 12:52:00 +0000" <-- same date, now in UTC


var dateFormatter = NSDateFormatter()
dateFormatter.dateFormat = "yyyy-MM-dd HH:mm:ss ZZZ"
let dateFromString = dateFormatter.dateFromString(utcTimeZoneStr)

let calendar = NSCalendar.currentCalendar()
let components = calendar.components([.Hour, .Minute], fromDate: date)
let hour = components.hour as Int
let minutes = components.minute

var dateComparisionResult:NSComparisonResult = NSDate().compare(date)


if dateComparisionResult == NSComparisonResult.OrderedAscending

{
    // Current date is smaller than end date.
    print("hello 1")
}

else if dateComparisionResult == NSComparisonResult.OrderedDescending

{
    // Current date is greater than end date.
    print("hello 2")

}
else if dateComparisionResult == NSComparisonResult.OrderedSame

{
    // Current date and end date are same.
    print("hello 3")

}



extension Double {
    var km: Double { return self * 1_000.0 }
    var m: Double { return self }
    var cm: Double { return self / 100.0 }
    var mm: Double { return self / 1_000.0 }
    var ft: Double { return self / 3.28084 }
}

let oneInch = 25.4.mm
print("One inch is \(oneInch) meters")

// prints "One inch is 0.0254 meters"
let threeFeet = 3.ft

print("Three feet is \(threeFeet) meters")
// prints "Three feet is 0.914399970739201 meters"



let names = ["Chris", "Alex", "Ewa", "Barry", "Daniella"]

func backwards(s1: String, _ s2: String) -> Bool {
    return s1 > s2
}

var reversed = names.sort(backwards)

reversed = names.sort( { (s1: String, s2: String) -> Bool in return s1 > s2 } )
print(reversed)

