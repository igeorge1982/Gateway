spec(function() {
     
     var queries = {
     tableView: { 'class': 'UITableView' },
     nameTextField: { 'class': 'UITextField', 'properties' : { 'tag': 1 } },
     companyTextField: { 'properties' : { 'placeholder': 'Company' } },
     phoneTextField: { 'properties' : { 'placeholder': 'Phone' } }
     };
     
     var stepRepository = {
     'Given DemoApplication is running': {
     'ios': [
             ios.launch('tsdemoapplication://')
             ]
     },
     "Go to text field demo": {
     'ios': [
             ios.scrollToRow(queries.tableView, 4, 0, ios.constants.tableViewScrollPosition.middle),
             ios.selectRow(queries.tableView, 4, 0)
             ]
     },
     "Type message in first text field": {
     'ios': [
             ios.typeText(queries.nameTextField, "Bill", 200),
             ios.typeText(queries.companyTextField, "Telerik", 200),
             ios.typeText(queries.phoneTextField, "555-555-5555", 200)
             ]
     }
     };
     
     describe("Verify keyboard automation functions correctly", function() {
              test("Type in text field", function() {
                   step('Given DemoApplication is running');
                   step("Go to text field demo");
                   step("Type message in first text field");
                   });
              }, stepRepository);
     
     });