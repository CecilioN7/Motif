<?php
class Database
{
	protected $conn = null;
	public function __construct()
	{
		try{
			$this->conn = new MySQLi(DB_HOST, DB_USERNAME, DB_PASSWORD, DB_NAME, PORT);
			if (mysqli_connect_errno()) {
				throw new Exception("Couldn't connect to database.");
			}
		} catch (Exception $e) {
			throw new Exception($e->getMessage());
		}
	}
	public function select($query = "", $params = []){
		try{
			$stmt = $this->executeStatement($query, $params);
			$result = $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
			$stmt->close();

			return $result;
		
		} catch (Exception $e){
			throw new Exception($e->getMessage());
		}
		return false;
	}
	public function insert($query = "", $params = []){
		try{
			$stmt = $this->executeStatement($query, $params);
			$stmt->close();
		} catch (Exception $e){
			throw new Exception($e->getMessage());
			return false;
		}
		return true;	
	}
	public function update($query = "", $params = []){
		try{
			$stmt = $this->executeStatement($query, $params);
			$stmt->close();
		} catch (Exception $e){
			throw new Exception($e->getMessage());
			return false;
		}
		return true;
	}
	public function delete($query = "", $params = []){
		try{
			$stmt = $this->executeStatement($query, $params);
			$stmt->close();

		} catch(Exception $e){
			throw new exception($e->getMessage());
			return false;
		}
		return true;
	}

	private function executeStatement($query = "", $params = []){
		try{	
			$stmt = $this->conn->prepare($query);

			if ($stmt === false){
				throw new Exception("Unable to do prepared statement: " . $query);
			}
			if ($params){		
				if (is_array($params[1])){
					if (sizeof($params[1]) == 2){
						$userID = $params[1][0];
						$userFollowedID = $params[1][1];
						$stmt->bind_param($params[0], $userID, $userFollowedID);
					} elseif (sizeof($params[1]) == 5){
						$username = $params[1][0];
						$password = $params[1][1];
						$email = $params[1][2];
						$name = $params[1][3];
						$phone = $params[1][4];
						$stmt->bind_param($params[0], $username, $password, $email, $name, $phone);
					}	
				} else {
					$stmt->bind_param($params[0], $params[1]);
				}
			}
			$stmt->execute();
			return $stmt;
		} catch(Exception $e){
			throw New Exception($e->getMessage());
		}
	}
}
			
			
