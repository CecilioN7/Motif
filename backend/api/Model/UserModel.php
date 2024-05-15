<?php

require_once PROJECT_ROOT_PATH . "/Model/Database.php";
class UserModel extends Database
{
	public function getAllUsers($limit){
		return $this->select("SELECT * FROM user order by ID ASC LIMIT ?", ["i", $limit]);
	}
	public function getUser($ID){
		return $this->select("SELECT * FROM user where ID = ?", ["i", $ID]);
	}
	public function getID($user){
		return $this->select("SELECT ID FROM user where username = ?", ["s", $user]);
	}
	public function changePassword($password, $user){
		$param = Array($password, $user);
		return $this->update("UPDATE user SET password = ? where username = ?", ["ss", $param]);
	}
	public function searchUser($search){
		return $this->select("SELECT * FROM user where username = ?", ["s", $search]);
	}
	public function addUser($username, $password, $email, $name, $phone){
		$hashed = hash('sha256', $password);
		$param = Array($username, $hashed, $email, $name, $phone);
		//print_r($params);
		return $this->insert("INSERT INTO user(username, password, email, fullName, phoneNumber) values (?, ?, ?, ?, ?)", ["sssss", $param]);
	}
}

?>
