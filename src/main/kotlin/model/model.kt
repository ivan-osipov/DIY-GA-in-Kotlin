package model

import java.util.*
import kotlin.collections.ArrayList

val DAYS = 6

val LESSONS = 10

typealias Schedule = Matrix<Event?>

class Data(val teachers: List<Teacher>,
           val students: List<Student>,
           val rooms: List<Room>)

open class Entity(var name: String, val id: UUID = UUID.randomUUID())

class Teacher(name: String, val disciplines: List<Discipline>) : Entity(name)

class Student(name: String, val studyLoad: List<Course>) : Entity(name)

class Group(val name: String, val course: Course, val students: List<Student>)

class Room(name: String, var capacity: Int) : Entity(name)

data class Discipline(val name: String)

data class Course(val discipline: Discipline, val capacity: Int)

data class Event(val teacher: Teacher, val group: Group, val day: Int, val lesson: Int)

open class Matrix<T>(val width: Int, val height: Int, init: (Int, Int) -> T) : Iterable<List<T>> {
    private val content: List<MutableList<T>>

    init {
        content = (0 until width).mapTo(ArrayList()) { row ->
            (0 until height).mapTo(ArrayList()) { column -> init(row, column) }
        }
    }

    override fun iterator() = content.iterator()

    operator fun get(i: Int, j: Int) = content[i][j]

    operator fun set(i: Int, j: Int, value: T) {
        content[i][j] = value
    }

    val T: Matrix<T>
        get() = Matrix(height, width) { i, j -> get(j, i) }

    override fun toString() = buildString {
        for(i in 0 until height) {
            for(j in 0 until width) {
                append(content[j][i]).append("\t\t")
            }
            append("\n")
        }
    }
}