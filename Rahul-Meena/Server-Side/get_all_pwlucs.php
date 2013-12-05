<?php // get_all_uwlics.php

// include db handler
require_once 'db_functions.php';
$db = new DB_Functions();

// array for JSON response
$response = array();
 
// check for required fields
if (isset($_POST['uid'])) {

    $uid = $_POST['uid'];

    // look for all pwlucs
    // false = not existed
    $rpwlucs = $db->getAllPWLUCSByUid($uid);

    // list found
    if($pwlucs){
        // list found success
        $response["success"] = 1;
        $response["people"] = array();

        // push pwlucs into final response array
        array_push($response["people"], $pwlucs["people"]);
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



