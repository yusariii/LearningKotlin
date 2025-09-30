fun main() {
    println("Nhập số phân số: ")
    val n = readln().toInt()
    var mangPhanSo = Array(n) { i ->
        println("Nhập phân số thứ ${i+1}")
        println("Nhập tử số: ")
        var tuSo = readln().toInt()
        while (tuSo == 0){
            println("Nhập lại mẫu số: ")
            tuSo = readln().toInt()
        }
        println("Nhập mẫu số: ")
        var mauSo = readln().toInt()
        while (mauSo == 0){
            println("Nhập lại mẫu số: ")
            mauSo = readln().toInt()
        }
        PhanSo(tuSo, mauSo)
    }

    println("In mảng phân số: ")
    mangPhanSo.forEach { it.inPhanSo() }

    println("In mảng phân số sau khi tối giản: ")
    mangPhanSo.forEach { it.toiGianPhanSo() }
    mangPhanSo.forEach { it.inPhanSo() }

    var tong = PhanSo(0 ,1)
    mangPhanSo.forEach { tong = tong.cong(it) }
    println("Tổng các phân số: ")
    tong.inPhanSo()

    var mangSapXep = mangPhanSo.sortedWith { ps1, ps2 -> ps2.soSanh(ps1) }
    println("Phân số lớn nhất: ")
    mangSapXep[0].inPhanSo()

    println("In mảng phân số đã sắp xếp giảm dần: ")
    mangSapXep.forEach { it.inPhanSo() }
}