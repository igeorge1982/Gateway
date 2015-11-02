function sayHello() {
    alert("Hello World")
}

var linebreak = "<br />";
document.write(linebreak);

var myVar = "global";    // Declare a global variable

function checkscope( ){
    var myVar = "local";  // Declare a local variable
    document.write(myVar);
}

document.write(linebreak);
