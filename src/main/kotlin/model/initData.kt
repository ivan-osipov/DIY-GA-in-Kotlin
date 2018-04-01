package model

fun createData(): Data {

    val math = Discipline("Math")

    val rus = Discipline("Rus")

    val student1 = Student("Smith", listOf(Course(math, 3), Course(rus, 4)))

    val mathTeacher = Teacher("Math Teacher", listOf(math))

    val rusTeacher = Teacher("Rus Teacher", listOf(rus))

    val smallSizeRoom = Room("101", 2)

    val bigSizeRoom = Room("102", 5)

    return Data(
            listOf(mathTeacher, rusTeacher),
            listOf(student1),
            listOf(smallSizeRoom, bigSizeRoom)
    )
}