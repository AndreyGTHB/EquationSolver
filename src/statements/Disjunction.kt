package statements

class Disjunction(body: Set<Rule>) : LongRule(body) {
    constructor(vararg body: Rule) : this(body.toSet())

    override fun simplify(): Rule {
        TODO("Not yet implemented")
    }

    override fun _union(other: Rule): Rule {
        return if (other is Disjunction) Disjunction(this.body + other.body)
               else                      Disjunction(this.body + other)
    }

    override fun unaryMinus() = Conjunction(body.map { -it }.toSet())

}