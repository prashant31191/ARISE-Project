<?php // adduwcsml.php

// include db handler
require_once 'db_functions.php';
$db = new DB_Functions();

// array for JSON response
$response = array();
 
// check for required fields
if (isset($_POST['uid1']) && isset($_POST['email2'])) {

    $uid1 = $_POST['uid1'];
    $email2 = $_POST['email2'];

    // add new person to pwcsul
    $add = $db->addPWCSULByUid($uid,$pemail);

    // new user
    if($add){
            // user successfully added
            $response["success"] = 1;
            $response["message"] = "User successfully added.";
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



