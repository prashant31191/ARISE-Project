<?php
 
class DB_Functions {
 
    private $db;
    public $tbl_user = "user";
    public $tbl_temp_user = "temp_user";
    public $tbl_change_password_user = "change_password_user";
    public $tbl_pwlucs_ = "pwlucs_";
    public $tbl_pwcsul_ = "pwcsul_";
    public $tbl_chat_pair_con = "chat_pair_con";
    public $tbl_chat_log_ = "chat_log_";
    public $tbl_static_var = "static_var";
 
    //put your code here
    // constructor
    function __construct() {
        require_once 'DB_Connect.php';
        // connecting to database
        $this->db = new DB_Connect();
        $this->db->connect();
    }
 
    // destructor
    function __destruct() {
         
    }
 
    /**
     * Storing new user
     * returns user details
     */
    public function createUser($name, $email, $password, $gcm_regid) {
        // Random confirmation code 
        $code=md5(uniqid(rand())); 
        $hash = $this->hashSSHA($password);
        $password = $hash["encrypted"]; // encrypted password
        $salt = $hash["salt"]; // salt
        // data in original user table
        $result1 = mysql_query("INSERT INTO $this->tbl_user(name, email, password, salt, gcm_regid) VALUES('$name', '$email', '$password', '$salt', '$gcm_regid')");
        // get uid for this user
        $sql = mysql_query("SELECT uid FROM $this->tbl_user WHERE email = '$email'");
        $row = mysql_fetch_array($sql);
        $uid = $row['uid'];
        echo $uid;
        //data in temp_user table
        $result2 = mysql_query("INSERT INTO $this->tbl_temp_user(uid,code) VALUES('$uid','$code')");
        // create pwlucs_uid table to store pwlucs and corresponding chat id pairs
        $temp_pwlucs_uid = $this->tbl_pwlucs_.$uid;
        echo $temp_pwlucs_uid;
        $result3 = mysql_query("CREATE TABLE $temp_pwlucs_uid(uid INT(10) UNSIGNED,chat_id INT(10) UNSIGNED)");
        // create pwcsul_uid table to store pwcsul
        $temp_pwcsul_uid = $this->tbl_pwcsul_.$uid;
        echo $temp_pwcsul_uid;
        $result4 = mysql_query("CREATE TABLE $temp_pwcsul_uid(uid INT(10) UNSIGNED)");
        // insert the table names in user table
        $result5 = mysql_query("INSERT INTO $this->tbl_user(pwlucs_table_name, pwcsul_table_name) VALUES('$temp_pwlucs_uid', '$temp_pwcsul_uid')");
        // check for successful store
        if ($result1 && $result2 && $result3 && $result4 && $result5) {
            // successfully inserted and send confirmation link
            $topic = "confirmation";
            $mail = $this->sendConfirmationLink($email,$code,$topic);
            if($mail){
                return true;
            } else{
                return false;
            }
        } else {
            echo "result prob";
            return false;
        }
    }
 
    /**
     * Send confirmation link to the given email
     * returns true/false
     */
    public function sendConfirmationLink($email,$code,$topic) {
        // if suceesfully inserted data into database, send confirmation link to email 
        // ---------------- SEND MAIL FORM ----------------

        // send e-mail to ...
        $to=$email;

        // Your subject
        $subject="Your $topic link";

        // From
        $header="from: Rahul Meena <rahul184.iitr@gmail.com>";

        // Your message
        $message="Your $topic link \r\n";
        if($topic == "confirmation"){
            $message.="Click on this link to activate your account \r\n";
            $message.="http://10.0.2.2/users/confirmation.php?passkey=$code";
        } else {
            $message.="Click on this link to change password of your account \r\n";
            $message.="http://10.0.2.2/users/change_password.php?passkey=$code";
        }
        // send email
        $sentmail = mail($to,$subject,$message,$header);

        if($sentmail){
            // confirmation link is sent successfully 
            return true;
        } else{
            //confirmation link not sent
            return false;
        }
    }
 
    /**
     * Get user by email and password
     */
    public function getUserByEmailAndPassword($email, $password) {
        // state: 0 - invalid account, 1 - login, 2 - invalid password or email

        $result = mysql_query("SELECT * FROM $this->tbl_user WHERE email = '$email'") or die(mysql_error());
        // check for result 
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            $result = mysql_fetch_array($result);
            $salt = $result['salt'];
            $password = $result['password'];
            $hash = $this->checkhashSSHA($salt, $password);
            // check for account being valid
            if($result["valid"] == 1){
                // check for password equality
                if ($password == $hash) {
                    // user authentication details are correct
                    $result["state"] = 1;
                    return $result;
                } else {
                    // password did not match
                    $result["state"] = 2;
                    return $result;
                }
            } else {
                // invalid account(not activated)
                $result["state"] = 0;
                return $result;
            }
        } else {
            // user not found
            return false;
        }
    }
 
    /**
     * Get user details by uid
     * return user details array
     */
    public function getUserDetailsByUid($uid) {
        $result = mysql_query("SELECT name,email,photo FROM $this->tbl_user WHERE uid = '$uid'") or die(mysql_error());
        // check for result 
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows == 1) {
            return $result;
        } else {
            // user not found
            return false;
        }
    }
 
    /**
     * Get user id from email
     * return email
     */
    public function getUserUidByEmail($email) {
        // person pid
        $pid = mysql_query("SELECT uid FROM $this->tbl_user WHERE email = '$email'") or die(mysql_error());
        // check for result 
        if ($pid) {
            return $pid;
        } else {
            // user not found
            return false;
        }
    }
 
    /**
     * Get last known location by uid
     * return user's last known location details array
     */
    public function getLastKnownLocationByUid($uid) {
        $result = mysql_query("SELECT loclat, loclong FROM $this->tbl_user WHERE uid = '$uid'") or die(mysql_error());
        // check for result 
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows == 1) {
            $user = array();
            $user["loclat"] = $result["loclat"];
            $user["loclong"] = $result["loclong"];
            return $user;
        } else {
            // user not found
            return false;
        }
    }
 
    /**
     * Get all people who can see user's location
     * return user details array
     */
    public function getAllPWCSULByTableName($pwcsul_table_name) {
        $result = mysql_query("SELECT uid FROM $pwcsul_table_name") or die(mysql_error());
        // check for result 
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            // users node
            $response["people"] = array();
            //looping through all the users emails
            foreach ($result as $value) {
                $result1 = $this->getUserDetailsByUid($value);
                // temp user array
                $user = array();
                $user["uid"] = $value;
                $user["name"] = $result1["name"];
                $user["email"] = $result1["email"];
                $user["photo"] = $result1["photo"];
 
                // push single user into final response array
                array_push($response["people"], $user);
            }
            return $response;
        } else {
            // user not found
            return false;
        }
    }
 
    /**
     * Get all people whose location user can see
     * return user details array
     */
    public function getAllPWLUCSByUid($uid) {
        // pwlucs table name
        $temp_pwlucs_uid = CONCATE($this->tbl_pwlucs_,$uid);
        $result = mysql_query("SELECT * FROM $temp_pwlucs_uid") or die(mysql_error());
        // check for result 
        if ($result > 0) {
            // users node
            $response["people"] = array();
            //looping through all the users emails
            foreach ($result["uid"] as $value) {
                $result1 = mysql_query("SELECT name, email, gcm_regid, photo, loclat, loclong FROM $this->tbl_user WHERE uid = '$value'");
                // temp user array
                $user = array();
                $user["cpid"] = $result["cpid"];
                $user["name"] = $row["name"];
                $user["email"] = $row["email"];
                $user["gcm_regid"] = $row["ngcm_regid"];
                $user["loclat"] = $row["loclat"];
                $user["loclong"] = $row["loclong"];
 
                // push single user into final response array
                array_push($response["people"], $user);
            }
            return $response;
        } else {
            // user not found
            return false;
        }
    }
 
    /**
     * Get chat log of the particular day
     * return chat log
     */
    public function getChatLogOfThatDay($cpid) {
        // chat log table for this cpid
        $temp_chat_log_cpid = CONCATE($this->tbl_chat_log_, $cpid);
        $result = mysql_query("SELECT * FROM $temp_chat_log_cpid") or die(mysql_error());
        // check for result 
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            // users node
            $response["chat_log"] = array();
            //looping through all the users emails
            foreach ($result as $value) {
                // temp chat array
                $chat = array();
                $chat["sender"] = $row["sender"];
                $chat["receiver"] = $row["receiver"];
                $chat["message"] = $row["message"];
                $chat["time_stamp"] = $row["time_stamp"];
 
                // push single user into final response array
                array_push($response["chat_log"], $chat);
            }
            return $response;
        } else {
            // user not found
            return false;
        }
    }
 
    /**
     * Update location by uid
     * return true/false
     */
    public function updateLocationByEmail($uid,$loclat,$loclong) {
        $result = mysql_query("UPDATE TABLE $this->tbl_user SET loclat = '$loclat',loclong = '$loclong' WHERE uid = '$uid'") or die(mysql_error());
        // check for result 
        if ($result) {
            return true;
        } else {
            // user not found
            return false;
        }
    }
 
    /**
     * Upload user image
     * return trur/false
     */
    public function uploadImage($uid,$image) {
        // check for result 
        if ($result) {
            //supported file extensions
            $file_exts = array("jpg", "jpeg", "png");
            //file ext of uploaded file
            $upload_exts = end(explode(".", $image["file"]["name"]));
            //match the format
            if ((($image["file"]["type"] == "image/jpg") || ($image["file"]["type"] == "image/jpeg") || ($image["file"]["type"] == "image/png"))
                && ($image["file"]["size"] < 2000000) && in_array($upload_exts, $file_exts)) {
                // check for error
                if ($image["file"]["error"] > 0) {
                    return false;
                } else {
                    //upload image
                    move_uploaded_file($_FILES["file"]["tmp_name"],"/upload/" . $uid);
                }
            } else {
                // image format not supported
                return false;
            }
        } else {
            // user not found
            return false;
        }
    }
 
    /**
     * Add Person who can see user location
     * return true/false
     */
    public function addPWCSULByUid($uid1,$email2) {
        // get other person uid
        $uid2 = $this->getUserUidByEmail($email2);
        // if a chat piar connnection already exist for this user pair
        $CPC_exist = $this->doesCPCExist($uid2,$uid1);
        if(!$CPC_exist){
            // insert into person's pwlucs table
            $temp_pwlucs_uid = CONCATE($this->tbl_pwlucs_,$pid);
            $chat_id = $this->addNewChatPairCon($uid,$pid);
            $result1 = mysql_query("INSERT INTO $temp_pwlucs_uid (uid, chat_id) VALUES ('$uid', '$chat_id')");
            // inser into user's pwcsul table 
            $temp_pwcsul_uid = CONCATE($this->tbl_pwcsul_,$uid);
            $result2 = mysql_query("INSERT INTO $temp_pwcsul_uid (uid) VALUES ('$pid')");
            // check for result 
            if ($result1 && $result2) {
                return true;
            } else {
                // user not found
                return false;
            }
        } else{
            $cpid = $this->getCPId($uid1,$uid2);
            // insert into person's pwlucs table
            $temp_pwlucs_uid = CONCATE($this->tbl_pwlucs_,$pid);
            $result1 = mysql_query("INSERT INTO $temp_pwlucs_uid (uid, cpid) VALUES ('$uid', '$cpid')");
            // inser into user's pwcsul table 
            $temp_pwcsul_uid = CONCATE($this->tbl_pwcsul_,$uid);
            $result2 = mysql_query("INSERT INTO $temp_pwcsul_uid (uid) VALUES ('$pid')");
            // update con value in chat pair conncetion table for this cpid
            $result3 = mysql_query("UPDATE TABLE $this->tbl_chat_pair_con SET con = 2 WHERE cpid = '$cpid'") or die(mysql_error());
            // check for result 
            if ($result1 && $result2 && $result3) {
                return true;
            } else {
                // user not found
                return false;
            }

        }
    }
 
    /**
     * Check if Chat pair connection already exist or not
     * return true/false
     */
    public function doesCPCExist($uid2, $uid1) {
        $temp_pwcsul_uid = CONCATE($this->tbl_pwcsul_,$uid2);
        $cpc = mysql_query("SELECT COUNT(*) FROM $temp_pwcsul_uid WHERE uid = '$uid1'") or die(mysql_error());
        // check for result 
        if ($cpc) {
            return true;
        } else {
            // user not found
            return false;
        }
    }
 
    /**
     * Get Chat pair id between user pair
     * return chat pair id
     */
    public function getCPId($uid1, $uid2) {
        $temp_pwlucs_uid = CONCATE($this->tbl_pwlucs_,$uid1);
        $cpid = mysql_query("SELECT cpid FROM $temp_pwlucs_uid WHERE uid = '$uid2'") or die(mysql_error());
        // check for result 
        if ($cpid) {
            return $cpid;
        } else {
            // user not found
            return false;
        }
    }
 
    /**
     * Add new user chat connection
     * return chat pair id
     */
    public function addNewChatPairCon($uid,$pid) {
        $result1 = mysql_query("INSERT INTO $this->tbl_chat_pair_con (u1,u2,con) VALUES ('$uid', '$pid', 1)");
        // get cpid for the just created chat pair
        $cpid = mysql_query("SELECT cpid FROM TABLE $this->tbl_chat_pair_con WHERE u1 = '$uid' && u2 = '$pid'") or die(mysql_error());
        // create corresponding chat table
        $temp_chat_log_cpid = CONCATE($this->tbl_chat_log_,$cpid);
        $result2 = mysql_query("CREATE TABLE $temp_chat_log_cpid(sender text, receiver text, message text, date_time_stamp timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP");
        // check for results
        if ($result1 && $result2) {
            return $cpid;
        } else {
            // user not found
            return false;
        }
    }
 
    /**
     * Adjust cpc table
     * return true/false
     */
    public function adjustCPC($cpid) {
        $con = mysql_query("SELECT con FROM TABLE $this->tbl_chat_pair_con WHERE cpid= '$cpid'") or die(mysql_error());
        // if con = 1
        if($con == 1){
            // delete row from the cpc table
            $delete = mysql_query("DELETE FROM TABLE $this->tbl_chat_pair_con WHERE cpid= '$cpid'") or die(mysql_error());
            if($delete){
                return true;
            } else {
                return false;
            }
        } else {
            // update the value of con to 1
            $update = mysql_query("UPDATE TABLE $this->tbl_chat_pair_con SET con = 1 WHERE cpid = '$cpid'") or die(mysql_error());
            if($update){
                return true;
            } else {
                return false;
            }

        }
    }
 
    /**
     * Remove Person who can see user location
     * return true/false
     */
    public function removePWCSULByUid($uid1,$email2) {
        // get other person uid
        $uid2 = $this->getUserUidByEmail($email2);
        // delete row into user's pwcsul table 
        $temp_pwcsul_uid = CONCATE($this->tbl_pwcsul_,$uid1);
        $result1 = mysql_query("DELETE FROM $temp_pwcsul_uid WHERE uid = '$uid2'");
        // person's pwlucs table
        $temp_pwlucs_uid = CONCATE($this->tbl_pwlucs_,$uid2);
        // get cpid first from person's pwlucs table
        $cpid = mysql_query("SELECT cpid FROM $temp_pwlucs_uid WHERE uid = '$uid1'") or die(mysql_error());
        // delete row into person's pwlucs table
        $result2 = mysql_query("DELETE FROM $temp_pwlucs_uid WHERE uid = '$uid1'") or die(mysql_error());
        // adjust the chat pair conncetion
        $adjust_cpc = $this->adjustCPC($cpid);
        // check for results
        if ($result1 && $result2 && $adjust_cpc) {
            return true;
        } else {
            // user not found
            return false;
        }
    }
 
    /**
     * Send change password link
     */
    public function sendChangePasswordLink($email) {
        // get user id
        $uid = $this->getUserUidByEmail($email);
        // Random confirmation code 
        $code=md5(uniqid(rand())); 
        //data in change_password_user table
        $result = mysql_query("INSERT INTO $this->tbl_change_password_user(uid,code) VALUES('$uid','$code')");
        // check for successful store
        if ($result) {
            // successfully inserted and send confirmation link
            $topic = "change password";
            $sentmail = $this->sendConfirmationLink($email,$code,$topic);
            if($sentmail){
                return true;
            } else{
                return false;
            }
        } else {
            return false;
        }
    }
 
    /**
     * Set new password
     */
    public function setNewPassword($passkey,$password) {
        // Retrieve data from table where row that match this passkey 
        $sql="SELECT uid FROM $this->tbl_change_password_user WHERE code ='$passkey'";
        $result=mysql_query($sql);

        // Count how many row has this passkey
        $count=mysql_num_rows($result);

        // If successfully queried 
        if($result && $count==1){
            // if found this passkey in our database, retrieve data from table
            $uid=$rows['uid'];
            // encrypt password
            $hash = $this->hashSSHA($password);
            $password = $hash["encrypted"]; // encrypted password
            $salt = $hash["salt"]; // salt
            // Update valid value in original table user" 
            $sql1="UPDATE $this->tbl_user SET password = '$password', salt = '$salt' WHERE uid = '$uid'";
            $result1=mysql_query($sql1);
            // Delete the entry against this passkey in change_password_user table" 
            $sql2="DELETE FROM $this->tbl_change_password_user WHERE code='$passkey'";
            $result2=mysql_query($sql2);
            //check results
            if($result1 && $result2){
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
 
    /**
     * Send Chat message
     */
    public function sendChatMessage($chat) {
        // caht details
        $sender = $chat["sender"];
        $receiver = $chat["receiver"];
        $message = $chat["message"];
        // chat log table name
        $temp_chat_log_cpid = CONCATE($this->tbl_chat_log_, $chat['cpid']);
        // insert new message entry
        $result = mysql_query("INSERT INTO $temp_chat_log_cpid (sender,receiver,message,time_stamp) VALUES('$sender', $receiver, '$message')");
        if ($result) {
            // user not existed 
            return false;
        } else {
            // user existed
            return true;
        }
    }
 
    /**
     * Check user is existed or not
     */
    public function userExist($email) {
        $result = mysql_query("SELECT COUNT(*) FROM $this->tbl_user WHERE email = '$email'");
        $row = mysql_fetch_array($result);
        if ($row[0]  == 1) {
            // user email exist
            return true;
        } else {
            // user existed
            return false;
        }
    }
 
    /**
     * Encrypting password
     * @param password
     * returns salt and encrypted password
     */
    public function hashSSHA($password) {
 
        $salt = sha1(rand());
        $salt = substr($salt, 0, 10);
        $encrypted = base64_encode(sha1($password . $salt, true) . $salt);
        $hash = array("salt" => $salt, "encrypted" => $encrypted);
        return $hash;
    }
 
    /**
     * Decrypting password
     * @param salt, password
     * returns hash string
     */
    public function checkhashSSHA($salt, $password) {
 
        $hash = base64_encode(sha1($password . $salt, true) . $salt);
 
        return $hash;
    }
 
}
 
?>