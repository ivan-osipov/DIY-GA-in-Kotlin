package model

import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class Data(val teachers: List<Teacher>,
           val students: List<Student>,
           val rooms: List<Room>)

class Schedule {

}

open class Entity {
    val id: UUID = UUID.randomUUID()
    var name: String = "Default"
}

class Discipline : Entity()

class Course(val discipline: Discipline, val capacity: Int): Entity()

class Teacher : Entity() {
    val disciplines: MutableList<Discipline> = ArrayList()
}

class Student : Entity() {
    val eduPlan = EduPlan()
}

class EduPlan {
    val studyLoad: MutableList<Course> = ArrayList()
}

class Room : Entity() {
    var capacity: Int = 0
}


class Matrix {

}

