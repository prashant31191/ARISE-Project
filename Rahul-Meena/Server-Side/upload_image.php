<?php // upload_image.php

// include db handler
require_once 'db_functions.php';
$db = new DB_Functions();

// array for JSON response
$response = array();
 
// check for required fields
if (isset($_FILES['image']) && isset($_POST['uid'])) {

    $image = $_FILES['image'];
    $uid = $_POST['uid'];

    // upload image function
    $upload = $db->uploadImage($uid,$image);

    // check
    if($upload){
        // success
        $response["success"] = 1;
        $response["message"] = "Image successfully updated."
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



