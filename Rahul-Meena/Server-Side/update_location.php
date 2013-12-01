<?php // update_location.php

// include db handler
require_once 'db_functions.php';
$db = new DB_Functions();

// array for JSON response
$response = array();
 
// check for required fields
if (isset($_POST['email']) && isset($_POST['loclat']) && isset($_POST['loclong'])) {

    $email = $_POST['email'];
    $loclat = $_POST['loclat'];
    $loclong = $_POST['loclong'];

    // check if user already existed
    // false = not existed
    $location = $db->updateLocationByEmail($email,$loclat,$loclong);

    // location found
    if($location){
        // location updated successfully
        $response["success"] = 1;
        $response["message"] = "Location updated successfully.";
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



