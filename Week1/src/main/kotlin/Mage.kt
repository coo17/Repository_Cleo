class Mage: Human{

    constructor(name: String, hasMana: Boolean) : super(name,hasMana)
    override fun attack(){
        println("$name use the Fireball!")
    }

}


