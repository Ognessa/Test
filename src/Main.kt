import kotlin.random.Random

fun main(args : Array<String>) {
    /*
    Use only one function:
    humanGuessNum() or computerGuessNum()
    They can run each other
     */

    /**--------------version for IDE console--------------*/
    //Uncomment for start game with standart input

    /*println("Enter \"x\" to exit.")
    val game = Game()
    game.setStartValues()
    game.humanGuessNum()*/

    /**--------------version for kotlin playground--------------*/
    val gameInKotlinPlayground = GameInKotlinPlayground()

    gameInKotlinPlayground.setStartValues()
    if(args.isNotEmpty()) {
        gameInKotlinPlayground.sortValuesInArgs(args)

        println("\nGame started")
        gameInKotlinPlayground.setStartValues()
        gameInKotlinPlayground.humanGuessNum()
    }
    else{
        println("Arguments is empty.")
    }
}

/**
 * Class which used for play game in Kotlin playground
 */
class GameInKotlinPlayground : Game(){
    val symbols : ArrayList<String> = arrayListOf()
    val numbers : ArrayList<Int> = arrayListOf()

    fun sortValuesInArgs(args : Array<String>){
        val number = Number()
        for(i in args){
            if(number.checkInputSymbols(i))
                symbols.add(i)
            else if(number.checkInputNum(i))
                numbers.add(i.toInt())
        }
    }

    /**
     * Computer makes a number
     * Human trying to guess number
     */
    override fun humanGuessNum(){
        if(numbers.size > 0){
            println("Try to guess my number: ")
            val answer = numbers[0]
            numbers.removeAt(0)
            println("Answer: $answer")

            val result = number.checkHumansAnswer(answer)

            if(result){
                println("\nNow it's your turn to make a number!")
                number.generateNum()
                computerGuessNum()
            }
            else{
                humanGuessNum()
            }
        }
        else{
            println("You lose :(")
        }
    }

    /**
     * Human makes a number
     * Computer trying to guess number
     */
    override fun computerGuessNum() {
        if(symbols.size > 0){
            val num = number.getNum()
            println("Is it your number: $num?" +
                    "\nEnter:" +
                    "\n\t< - if your number is bigger than my" +
                    "\n\t> - if your number is less than my" +
                    "\n\t= - if this is your number")
            val answer : Char = (symbols[0])[0]
            symbols.removeAt(0)
            println("Answer: $answer")

            val result = number.checkComputersAnswer(answer)

            if(result){
                println("\nNow it's my turn to make a number!")
                setStartValues()
                humanGuessNum()
            }
            else{
                if(answer == '<') number.setMin(num)
                else number.setMax(num)

                number.generateNum()
                computerGuessNum()
            }
        }
        else{
            println("I'm lose :(")
        }
    }
}

/**
 * Class which used for play game in standard console
 */
open class Game{
    val number = Number()

    fun setStartValues(){
        number.setMin(0)
        number.setMax(100)
        number.generateNum()
    }

    /**
     * Computer makes a number
     * Human trying to guess number
     */
    open fun humanGuessNum(){
        println("Try to guess my number: ")
        val answer : String = readLine().toString()

        wantExit(answer)

        val isNum = number.checkInputNum(answer)
        if(!isNum){
            humanGuessNum()
        }
        else{
            val result = number.checkHumansAnswer(answer.toInt())
            if(result){
                println("\nNow it's your turn to make a number!")
                number.generateNum()
                computerGuessNum()
            }
            else humanGuessNum()
        }
    }

    /**
     * Human makes a number
     * Computer trying to guess number
     */
    open fun computerGuessNum(){
        val num = number.getNum()
        println("Is it your number: $num?" +
                "\nEnter:" +
                "\n\t< - if your number is bigger than my" +
                "\n\t> - if your number is less than my" +
                "\n\t= - if this is your number")
        val answer : String = readLine().toString()

        wantExit(answer)

        val isNum = number.checkInputSymbols(answer)
        if(!isNum){
            computerGuessNum()
        }
        else{
            val result = number.checkComputersAnswer(answer[0])
            if(result){
                println("\nNow it's my turn to make a number!")
                setStartValues()
                humanGuessNum()
            }
            else{
                if(answer[0] == '<') number.setMin(num)
                else number.setMax(num)

                number.generateNum()
                computerGuessNum()
            }
        }
    }

    /**
     * Check if user want close game
     */
    fun wantExit(answer : String){
        if(answer.equals("x")){
            System.exit(0)
        }
    }
}


class Number{
    private var number : Int = 0
    private var min : Int = 0
    private var max : Int = 100

    /**Generate random number*/
    fun generateNum(){
        number = Random.nextInt(min, max)
    }

    fun setMin(min : Int){ this.min = min}
    fun setMax(max : Int){ this.max = max}
    fun getNum() : Int{return number}

    /**
     * Check answer when computer make a number
     * */
    fun checkHumansAnswer(answer : Int) : Boolean{
        if(answer == number) {
            println("Yes, it`s my number!")
            return true
        }
        else if(answer < number) println("Your number is less than mine.")
        else println("Your number is bigger than mine.")
        return false
    }

    /**
     * Check answer when human make a number
     * */
    fun checkComputersAnswer(answer : Char) : Boolean{
        if(answer == '<' || answer == '>'){
            println("Oh, I`ll try again")
            return false
        }
        println("Yes, I win!")
        return true
    }

    /**
     * Check input when computer make a number
     * */
    fun checkInputNum(answer : String) : Boolean{
        answer.lowercase()
        //TODO don`t see letters
        if(answer.isEmpty() || answer.matches(Regex("[a-z]+"))){
            println("$answer isn't number. Try again.")
            return false
        }
        return true
    }

    /**
     * Check input when human make a number
     * */
    fun checkInputSymbols(answer: String) : Boolean{
        val symbol : Char = answer[0]
        if((symbol == '<' || symbol == '>' || symbol == '=') && answer.length == 1)
            return true
        println("Incorrect answer $answer. Enter \"<\", \">\" or \"=\".")
        return false
    }
}