<?php //login.php

// include db handler
require_once 'db_functions.php';
$db = new DB_Functions();

// array for JSON response
$response = array();

 
// check for required fields
if (isset($_POST['email']) && isset($_POST['password'])) {
 
    $email = $_POST['email'];
    $password = $_POST['password'];
 
        // check for user
        $user = $db->getUserByEmailAndPassword($email, $password);
        if ($user != false) {
            // user found
            $response["success"] = 1;
            $response["user"] = array();
            $response["user"]["uid"] = $user["uid"];
            $response["user"]["gcm_regid"] = $user["gcm_regid"];
            $response["user"]["name"] = $user["name"];
            $response["user"]["photo"] = $user["photo"];
            $response["user"]["pwcsul_table_name"] = $user["pwcsul_table_name"];
            $response["user"]["pwlucs_table_name"] = $user["pwlics_table_name"];
            $pwcsul = $dv->getAllPWCSULByTableName($user["pwcsul_table_name"]);
            if($pwcsul){
                $response["people"] = array();
                // push pwcsul into final response array
                array_push($response["people"], $pwcsul["people"]);
            }
            echo json_encode($response);
        } else {
            // user not found
            $response["success"] = 0;
            $response["message"] = "Incorrect email or password!";
            echo json_encode($response);
        }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
}
?>