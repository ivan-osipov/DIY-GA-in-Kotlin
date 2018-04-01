package model

import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class Data(val teachers: List<Teacher>,
           val students: List<Student>,
           val rooms: List<Room>)

open class Entity(var name: String, val id: UUID = UUID.randomUUID())

class Teacher(name: String, val disciplines: List<Discipline>) : Entity(name)

class Student(name: String, val studyLoad: List<Course>) : Entity(name)

class Room(name: String, var capacity: Int) : Entity(name)


data class Discipline(val name: String)

data class Course(val discipline: Discipline, val capacity: Int)


