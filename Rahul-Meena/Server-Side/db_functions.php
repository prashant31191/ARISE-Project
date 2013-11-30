<?php
 
class DB_Functions {
 
    private $db;
    private const $tbl_temp_user = "temp_user";
    private const $tbl_change_password_user = "change_password_user";
    private const $tbl_user = "user";
 
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
    public function storeUser($name, $email, $password) {
        // Random confirmation code 
        $code=md5(uniqid(rand())); 
        $hash = $this->hashSSHA($password);
        $password = $hash["encrypted"]; // encrypted password
        $salt = $hash["salt"]; // salt
        //data in temp_user table
        $result1 = mysql_query("INSERT INTO $this->$tbl_temp_user(email,code) VALUES('$email','$code')");
        // data in original user table
        $result2 = mysql_query("INSERT INTO $this->$tbl_user(name, email, password, salt) VALUES('$name', '$email', '$password', '$salt')");
        // check for successful store
        if ($result1 && $result2) {
            // successfully inserted and send confirmation link
            $topic = "confirmation"
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
     * Send confirmation link to the given email
     * returns true/false
     */
    public function sendConfirmationLink($email,$code,$topic) {
        // if suceesfully inserted data into database, send confirmation link to email 
        // ---------------- SEND MAIL FORM ----------------

        // send e-mail to ...
        $to=$email;

        // Your subject
        $subject="Your '$topic' link here";

        // From
        $header="from: Rahul Meena <rahul184.iitr@gmail.com>";

        // Your message
        $message="Your '$topic' link \r\n";
        if($topic == "confirmation"){
            $message.="Click on this link to activate your account \r\n";
            $message.="http://10.0.2.2/users/confirmation.php?passkey='$code'";
        } else {
            $message.="Click on this link to change password of your account \r\n";
            $message.="http://10.0.2.2/users/change_password.php?passkey='$code'";
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
        $result = mysql_query("SELECT * FROM $this->$tbl_user WHERE email = '$email' AND valid = 1") or die(mysql_error());
        // check for result 
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            $result = mysql_fetch_array($result);
            $salt = $result['salt'];
            $password = $result['password'];
            $hash = $this->checkhashSSHA($salt, $password);
            // check for password equality
            if ($password == $hash) {
                // user authentication details are correct
                return $result;
            }
        } else {
            // user not found
            return false;
        }
    }
 
    /**
     * Send change password link
     */
    public function sendChangePasswordLink($email) {
        // Random confirmation code 
        $code=md5(uniqid(rand())); 
        //data in change_password_user table
        $result = mysql_query("INSERT INTO $this->$tbl_change_password_user(email,code) VALUES('$email','$code')");
        // check for successful store
        if ($result) {
            // successfully inserted and send confirmation link
            $topic = "change_password"
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
        $sql="SELECT * FROM $this->$tbl_change_password_user WHERE code ='$passkey'";
        $result=mysql_query($sql);

        // Count how many row has this passkey
        $count=mysql_num_rows($result);

        // If successfully queried 
        if($result && $count==1){
            // if found this passkey in our database, retrieve data from table
            $rows=mysql_fetch_array($result);
            $email=$rows['email'];
            // encrypt password
            $hash = $this->hashSSHA($password);
            $password = $hash["encrypted"]; // encrypted password
            $salt = $hash["salt"]; // salt
            // Update valid value in original table user" 
            $sql1="UPDATE $this->$tbl_user SET password = '$password', salt = '$salt' WHERE email = '$email'";
            $result1=mysql_query($sql1);
            // Delete the entry against this email in change_password_user table" 
            $sql2="DELETE FROM $this->$tbl_change_password_user WHERE code='$passkey'";
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
     * Check user is existed or not
     */
    public function isUserExisted($email) {
        $result = mysql_query("SELECT COUNT(*) FROM $this->$tbl_user WHERE email = '$email'");
        if ($result) {
            // user not existed 
            return false;
        } else {
            // user existed
            return true;
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