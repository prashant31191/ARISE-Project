<?php // get_user.php

// include db handler
require_once 'db_functions.php';
$db = new DB_Functions();

// array for JSON response
$response = array();
 
// check for required fields
if (isset($_POST['email'])) {

    $email = $_POST['email'];

    // check if user already existed
    // false = not existed
    $user = $db->getUserDetailsByEmail($email);

    // new user
    if($user){
        // user details
        $response["success"] = 1;
        $response["user"] = array();
        // push single user into final response array
        array_push($response["user"], $user);
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



