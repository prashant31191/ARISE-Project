<?php //sendlocation.php

require_once 'userslogin.php';
$db_server = mysql_connect($db_hostname, $db_username, $db_password);
if (!$db_server) die("Unable to connect to MySQL: " . mysql_error());
mysql_select_db($db_database, $db_server)
or die("Unable to select database: " . mysql_error());

// array for JSON response
$response = array();
 
// check for required fields
if (isset($_POST['email']) && isset($_POST['location'])) {
 
    $email = $_POST['email'];
    $location = $_POST['location'];

    // mysql update row with matched pid
    $result = mysql_query("UPDATE user SET location = '$location' WHERE email = '$email'");
 
    // check if row inserted or not
    if ($result) {
        // successfully updated
        $response["success"] = 1;
        $response["message"] = "Location successfully updated.";
 
        // echoing JSON response
        echo json_encode($response);
    } else {
 
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    echo json_encode($response);
}
?>