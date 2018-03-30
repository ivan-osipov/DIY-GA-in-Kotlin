package model

fun createData(): Data {

    val math = Discipline().apply {
        name = "Math"
    }

    val rus = Discipline().apply {
        name = "Rus"
    }

    val student1 = Student()
    student1.eduPlan.studyLoad.apply {
        add(Course(math, 3))
        add(Course(rus, 4))
    }

    val mathTeacher = Teacher()
    mathTeacher.name = "Math Teacher"
    mathTeacher.disciplines.add(math)

    val rusTeacher = Teacher()
    rusTeacher.name = "Rus Teacher"
    rusTeacher.disciplines.add(math)

    val smallSizeRoom = Room().apply {
        name = "101"
        capacity = 2
    }

    val bigSizeRoom = Room().apply {
        name = "102"
        capacity = 5
    }

    return Data(
            listOf(mathTeacher, rusTeacher),
            listOf(student1),
            listOf(smallSizeRoom, bigSizeRoom)
    )
}