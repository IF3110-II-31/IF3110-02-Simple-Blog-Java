function validate_date(){
    var string=document.getElementById("newpost:validateDate").value;		
    var tanggal_input = new Date();
    var year=string.substring(0,4);
    var month=string.substring(5,7);
    var date=string.substring(8,10);
    tanggal_input.setFullYear(year,month,date);
    var tanggal_sekarang=new Date();
    if(tanggal_input<tanggal_sekarang) return true;
    else return false;
}

function validate_new_post(){
	document.getElementById("newpost").onsubmit = function(){
		var Judul=document.getElementById("newpost:Judul").value;
		var Konten=document.getElementById("newpost:Konten").value;
                var tanggal=document.getElementById("newpost:validateDate").value;
		var errorMessage=document.getElementById("error");
		if(Judul === "" || Konten=== "" || tanggal=== "" || validate_date()) {
			if(validate_date()) errorMessage.innerHTML="*Tanggal harus tanggal hari ini atau sesudahnya";
			else errorMessage.innerHTML= "*Post tidak lengkp";
                        return false;			
		}
		else{
			return true;
		}
	}
}

function validate_email(email) { 
    var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(email);
} 

function validate_comment(){
    	document.getElementById("komen").onsubmit = function(){
		var Nama=document.getElementById("komen:Nama").value;
		var Email=document.getElementById("komen:Email").value;
                var Komen=document.getElementById("komen:Komentar").value;
		var errorMessage=document.getElementById("error");
		if(Nama === "" || Email=== "" || Komen=== "" || !validate_email(Email)) {
			if(!validate_email()) errorMessage.innerHTML="*Format Email tidak sesuai";
			else errorMessage.innerHTML= "*Post tidak lengkap";
                        return false;			
		}
		else{
			return true;
		}
	}
}
