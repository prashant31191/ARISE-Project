<?php // get_all_uwlics.php

// include db handler
require_once 'db_functions.php';
$db = new DB_Functions();

// array for JSON response
$response = array();
 
// check for required fields
if (isset($_POST['email'])) {

    $email = $_POST['email'];

    // look for all uwlics
    // false = not existed
    $response = $db->getAllUWLICSByEmail($email);

    // list found
    if($response){
        // list found success
        $response["success"] = 1;
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



