# zillowApp
The app functions as follows:

1. ######Initial User Interface:
  Provided an Android Activity (page) to take Address, City, and State as input. When the “Search” button is tapped, error checking is done, and on valid input, the JSON result from the PHP file located on my AWS server is returned. On all valid input, another activity shows the results page, which is a tabbed interface.

2. ######The Basic Info Tab:
  Dispalys a scrollable list detailing all the information about the property.
  
3. ######Facebook: 
  There is a button at the top-right of the “Basic Info” tab which is a Facebook share button. Clicking on it will allow one to post the property details on Facebook. Facebook handles the first time logins if required.
  
4. ######Historical Zestimates Tab:
  The second and only other tab shows the images of the Zestimate charts.
5. ######Error handling: 

  Error handling is done for empty fields or invalid input.
