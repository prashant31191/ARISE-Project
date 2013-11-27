<?php //register.php

require_once 'userslogin.php';
$db_server = mysql_connect($db_hostname, $db_username, $db_password);
if (!$db_server) die("Unable to connect to MySQL: " . mysql_error());
mysql_select_db($db_database, $db_server)
or die("Unable to select database: " . mysql_error());

// array for JSON response
$response = array();

// Random confirmation code 
$code=md5(uniqid(rand())); 
 
// check for required fields
if (isset($_POST['name']) && isset($_POST['email']) && isset($_POST['password'])) {
 
    $name = $_POST['name'];
    $email = $_POST['email'];
    $password = $_POST['password'];

    // mysql to get no of rows that matches the given email
    $check = mysql_query("SELECT * FROM user WHERE email = '$email'");

    //check if the account is already registered or not
    if($check == 0){

        // mysql inserting a new row
        $result = mysql_query("INSERT INTO user(name, email,password,code) VALUES('$name', '$email', '$password', '$code')");
 
        // check if row inserted or not
        if ($result) {

            // if suceesfully inserted data into database, send confirmation link to email 
            // ---------------- SEND MAIL FORM ----------------

            // send e-mail to ...
            $to=$email;

            // Your subject
            $subject="Your confirmation link here";

            // From
            $header="from: Rahul Meena <rahul184.iitr@gmail.com>";

            // Your message
            $message="Your Comfirmation link \r\n";
            $message.="Click on this link to activate your account \r\n";
            $message.="http://10.0.2.2/users/confirmation.php?passkey=$code";

            // send email
            $sentmail = mail($to,$subject,$message,$header);

            if($sentmail){
                // successfully inserted into database and confirmation link is sent
                $response["success"] = 1;
                $response["message"] = "User successfully registered.";
            }
 
        } else {
        // failed to insert row
        $response["success"] = 0;
        $response["message"] = "Oops! An error occurred.";
 
         }
    } else {
        // account already registered
        $response["success"] = 0;
        $response["message"] = "This account is already registered.";

    }

}    else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
}
    // echoing JSON response
    echo json_encode($response);
?>



