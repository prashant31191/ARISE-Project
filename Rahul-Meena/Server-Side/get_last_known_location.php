<?php // get_last_known_location.php

// include db handler
require_once 'db_functions.php';
$db = new DB_Functions();

// array for JSON response
$response = array();
 
// check for required fields
if (isset($_POST['uid'])) {

    $email = $_POST['uid'];

    // get last known location of user
    $location = $db->getLastKnownLocationByUid($uid);

    // location found
    if($location){
        // location details
        $response["success"] = 1;
        $response["location"] = array();
        // push single user into final response array
        array_push($response["location"], $location);
    } else {
            // failed to insert row
            $response["success"] = 0;
            $response["message"] = "Oops! An error occurred.";
    }

} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
}
    // echoing JSON response
    echo json_encode($response);
?>



