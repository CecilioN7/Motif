<?php

require_once PROJECT_ROOT_PATH . "/Model/Database.php";
class NotesheetsModel extends Database
{
	public function getUserSheets($ID){
		return $this->select("SELECT sheetID FROM notesheets where UID = ?", ["i", $ID]);
	}
	public function getSheet($ID){
		return $this->select("SELECT notes FROM notesheets where sheetID = ?", ["i", $ID]);
	}
	public function removeSheet($ID){
		return $this->delete("DELETE FROM notesheets where sheetID = ?", ["i", $ID]);
	}
	public function updateSheet($ID, $notes){
		$param = Array($notes, $ID);
		return $this->update("UPDATE notesheets SET notes = ? where sheetID = ?", ["si", $param]);
	}
	public function addSheet($UID, $notes){
        if (!is_int($UID)){
		    return 0;
        }
		$param = Array($UID, $notes);
		return $this->insert("INSERT INTO notesheets(UID, notes) values (?, ?)", ["is", $param]);
	}
}

?>
