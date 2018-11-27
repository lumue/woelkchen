
	function leastFactor(n) {
		if (isNaN(n) || !isFinite(n)) return NaN;
		if (typeof phantom !== 'undefined') return 'phantom';
		if (typeof module !== 'undefined' && module.exports) return 'node';
		if (n == 0) return 0;
		if (n % 1 || n * n < 2) return 1;
		if (n % 2 == 0) return 2;
		if (n % 3 == 0) return 3;
		if (n % 5 == 0) return 5;
		var m = Math.sqrt(n);
		for (var i = 7; i <= m; i += 30) {
			if (n % i == 0) return i;
			if (n % (i + 4) == 0) return i + 4;
			if (n % (i + 6) == 0) return i + 6;
			if (n % (i + 10) == 0) return i + 10;
			if (n % (i + 12) == 0) return i + 12;
			if (n % (i + 16) == 0) return i + 16;
			if (n % (i + 22) == 0) return i + 22;
			if (n % (i + 24) == 0) return i + 24;
		}
		return n;
	}

	function go() {
		var p = 3099024628308;
		var s = 3148355175;
		var n;
		if ((s >> 14) & 1) p +=/* 120886108*
*/2694565 */* 120886108*
*/17;/* 120886108*
*/ else /*
else p-=
*/p -=/*
p+= */109768335 */* 120886108*
*/15;
		if ((s >> 3) & 1)/*
p+= */p +=
				253211888 */*
else p-=
*/4; else /*
*13;
*/p -=/* 120886108*
*/500478415 */*
else p-=
*/4;
		if ((s >> 9) & 1)/*
p+= */p +=
				52136708 */*
*13;
*/10;
		else /*
else p-=
*/p -= 110986949 */*
else p-=
*/10;
		if ((s >> 12) & 1)/* 120886108*
*/p +=
				85502214 * 15; else /*
*13;
*/p -= 53059521 */* 120886108*
*/13;
		if ((s >> 7) & 1) p += 205719705 */*
p+= */8;/* 120886108*
*/ else /*
*13;
*/p -= 114723417 */*
*13;
*/8;/*
*13;
*/
		p += 2151914842;
		n = leastFactor(p);
		{
			document.cookie = "RNKEY=" + n + "*" + p / n + ":" + s + ":74430974:1";
			document.location.reload(true);
		}
	}

	//