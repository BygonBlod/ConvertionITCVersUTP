package Utils;

public class Value_USP {

	// Elements-----------------------------------------------------------------
	// Timmetabling childs
	public static String Time_Calendar = "calendar";
	public static String Time_Rooms = "rooms";
	public static String Time_Equipments = "equipments";
	public static String Time_Teachers = "teachers";
	public static String Time_Coursess = "courses";
	public static String Time_Students = "students";
	public static String Time_Rules = "rules";
	public static String Time_Solution = "solution";

	// rooms child
	public static String Rooms_Room = "room";

	// equipments child
	public static String Equipments_Equipment = "equipment";

	// teachers child
	public static String Teachers_Teacher = "teacher";

	// courses child
	public static String Courses_Course = "course";
	// course child
	public static String Course_Part = "part";
	// part child
	public static String Part_Classes = "classes";
	public static String Part_AllowedSlots = "allowedSlots";
	public static String Part_AllowedRooms = "allowedRooms";
	public static String Part_AllowedTeachers = "allowedTeachers";

	// classes child
	public static String Classes_Class = "class";
	// allowedSlots child
	public static String AllowedSlots_DailySlots = "dailySlots";
	public static String AllowedSlots_Days = "days";
	public static String AllowedSlots_Weeks = "weeks";
	// allowedRooms child
	public static String AllowedRooms_Room = "room";
	// allowedTeachers child
	public static String AllowedTeachers_Teacher = "teacher";

	// students child
	public static String Students_Student = "student";
	// student child
	public static String Student_Courses = "courses";
	// courses child
	public static String StudentCourses_Course = "course";

	// rules child
	public static String Rules_Rule = "rule";
	// rule child
	public static String Rule_Sessions = "sessions";
	public static String Rule_Constraint = "constraint";

	// sessions child
	public static String Sessions_Filter = "filter";
	// constraint child
	public static String Constraint_Parameters = "parameters";
	// parameters child
	public static String Prameters_Parameter = "parameter";

	// solution child
	public static String Solution_Groups = "groups";
	public static String Solution_Sessions = "sessions";

	// groups child
	public static String Groups_Group = "group";
	// group child
	public static String Group_Students = "students";
	public static String Group_Classes = "classes";
	// students child
	public static String SolutionStudents_Student = "student";
	// classes child
	public static String SolutionClasses_Class = "class";

	// sessions child
	public static String SolutionSessions_Session = "session";

	// Attributes --------------------------------------------------------------
	// use in multiple Elements
	public static String Attibute_Id = "id";
	public static String Attibute_Label = "label";
	public static String Attibute_RefId = "refId";
	public static String Attibute_Type = "type";
	public static String Attibute_AttributeName = "attributeName";
	public static String Attibute_In = "in";
	public static String Attibute_NotIn = "notIn";
	public static String Attibute_Name = "name";
	public static String Attibute_Capacity = "capacity";
	public static String Attibute_Count = "count";
	public static String Attibute_NrSessions = "nrSessions";
	public static String Attibute_MaxHeadCount = "maxHeadCount";
	public static String Attibute_Parent = "parent";
	public static String Attibute_SessionLength = "sessionLength";
	public static String Attibute_SessionRooms = "sessionRooms";
	public static String Attibute_Mandatory = "mandatory";
	public static String Attibute_SessionTeachers = "sessionTeachers";
	public static String Attibute_GroupBy = "groupBy";
	public static String Attibute_SessionsMask = "sessionsMask";
	public static String Attibute_HeadCount = "headCount";
	public static String Attibute_Class = "class";
	public static String Attibute_Rank = "rank";
	public static String Attibute_Slot = "slot";
	public static String Attibute_Rooms = "rooms";
	public static String Attibute_Teachers = "teachers";
	public static String Attibute_NrWeeks = "nrWeeks";
	public static String Attibute_NrDaysPerWeek = "nrDaysPerWeek";
	public static String Attibute_NrSlotsPerDay = "nrSlotsPerDay";
}
