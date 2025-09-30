data class PhanSo(var tuSo: Int, var mauSo: Int){
    init {
        if (mauSo < 0) {
            tuSo = -tuSo
            mauSo = -mauSo
        }
    }

    fun inPhanSo(){
        println("$tuSo / $mauSo")
    }

    fun toiGianPhanSo(){
        val ucln = ucln(tuSo, mauSo)
        tuSo /= ucln
        mauSo /= ucln
        if (mauSo < 0) {
            tuSo = -tuSo
            mauSo = -mauSo
        }
    }

    fun soSanh(a: PhanSo) : Int{
        val compare = this.tuSo * a.mauSo - a.tuSo * this.mauSo
        return when {
            compare > 0 -> 1
            compare < 0 -> -1
            else -> 0
        }
    }

    fun cong(a: PhanSo) : PhanSo{
        var bcnn = bcnn(this.mauSo, a.mauSo)
        var tu1 = bcnn / this.mauSo * this.tuSo
        var tu2 = bcnn / a.mauSo * a.tuSo
        return PhanSo(tu1 + tu2, bcnn)
    }
}

fun ucln(so1: Int, so2: Int) : Int{
    var a = so1
    var b = so2
    while (a != b){
        if ( a > b) a -= b
        else b -= a
    }
    return a
}

fun bcnn(so1: Int, so2: Int) : Int{
    var a = so1
    var b = so2
    return a * b / ucln(a, b)
}
