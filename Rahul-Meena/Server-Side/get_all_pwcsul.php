<?php // get_all_uwcsml.php

// include db handler
require_once 'db_functions.php';
$db = new DB_Functions();

// array for JSON response
$response = array();
 
// check for required fields
if (isset($_POST['email'])) {

    $email = $_POST['email'];

    // look for all pwcsul
    $pwcsul = $db->getAllPWCSULByUid($uid);

    // list found
    if($pwcsul){
        // list found success
        $response["success"] = 1;
        $response["people"] = array();

        // push pwcsul into final response array
        array_push($response["people"], $pwcsul["people"]);
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



