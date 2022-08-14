open class Human {
    var name: String = ""
    var hasMana: Boolean = true

    constructor(_name: String, _hasMana: Boolean) {
        name = _name
        hasMana = _hasMana
    }

    open fun attack(){
        println("$name use the First Attack!")
    }
    fun mana(){
        if(hasMana){
            println("$name 有魔力!")
        }else{
            println("$name 我沒魔了")
        }
    }
}

fun main(){
    var child = Human("Kuku",false)
    child.attack()
    child.mana()

    var man = Mage ("Lulu",true)
    man.attack()

}