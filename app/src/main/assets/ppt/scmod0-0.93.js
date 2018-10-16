var be = 0;
var advcp = 0;
var aJ;
var cY = 0;
var ag = -1;
var cF = -1;
var fI = -1;
var eg = -1;
var K;
var aS;
var aP;
var aW;
var dv;
var dK;
var bV;
var gd = 0;
var eG = 0;
var nk = 0;
var mD = 1;
var dA = 30;
var ew = 1;
var aG;
var aE = [];
var cT = new Array;
var bc;
var bm = 0;
var cN = 0;
var ie = 0;
var ja = 0;
var fq = 0;
var nt = 1;
var hG = -1;
var dN = null;
var eR = 0;
var eZ = 0;
var hF = 30;
var hx = "transform";
var gf = "transform-origin";
var dV = [];
var mW = 0;
var eY = 0;
var is_replay = 1;
var iS;
var dU = "";
var cn = 0;
var fe;
var hz;
var courseaudio;
var autoplay = 0;
var iscef = 0;
var mV = 0;
var addevt = 0;
var mz = 0;
var iF = 0;
var fm = 0;
var mY = 0;
var gZ = 0;
oY();
var gA = mL;
var autoPlayReset = mL;
var playFromPage = mL;
var jG = mL;
var om = mL;
var mA = mL;
var oM = mL;
function fC(o, v) {
    if (v.para >= 0) {
        var V = o.children[o.children.length - 1];
        if (!V) return o;
        var d = V.getElementsByTagName('div');
        if (d.length > v.para) {
            return d[v.para];
        } else {
            return null;
        }
    } else if (v.para == -1) {
        return o;
    } else if (v.para == -2) {
        var V = o.children[0];
        if (V && V.className == "ib") {
            return V;
        }
        return null;
    }
    return o;
};
function ka(az) {
    var d = document.getElementById("s" + az);
    d.style.height = aP + "px";
    d.style.width = aS + "px";
    d.style.position = "absolute";
    if (d.style.background == "") d.style.background == "#fff";
    if (window._control) {
        hu(d, az);
    }
    d.style.display = "none";
    if (!fm) d.style.overflow = "hidden";
};
function kN() {
    var aa = document.getElementsByTagName("span");
    for (var dm = 0; dm < aa.length; dm++) {
        var bM = aa.item(dm);
        if (mz && bM.style["font-weight"] == "bold") {
            bM.style["font-weight"] = "normal";
        }
        if (bM.className.indexOf("vchar") >= 0) {
            if (bM.children.length > 0) {
                var er = 0;
                for (var i = 0,
                hE = bM.children.length; i < hE; i++) {
                    var cW = bM.children[i];
                    cW.style.left = "0px";
                    cW.style.top = er + "px";
                    er += cW.offsetHeight;
                }
                continue;
            }
            var T = "";
            var er = 0;
            var gj = bM.innerHTML.replace(/<.+?>/gim, '');
            var Text = new Array();
            for (i = 0; i < gj.length; i++) {
                if (gj[i] == '&') {
                    var fi = i;
                    var bA = "";
                    while (gj[fi] != ';' && fi < gj.length) {
                        bA += gj[fi];
                        fi++;
                    }
                    bA += ';';
                    Text.push(bA);
                    i = fi;
                } else {
                    Text.push(gj[i]);
                }
            }
            bM.innerHTML = "";
            for (var i = 0,
            hE = Text.length; i < hE; i++) {
                var cW = document.createElement("span");
                cW.style.top = er + "px";
                cW.innerHTML = Text[i];
                bM.appendChild(cW);
                er += cW.offsetHeight;
            }
            continue;
        }
        if (bM.previousSibling && bM.previousSibling.tagName && bM.previousSibling.tagName.toLowerCase() == "span" && bM.parentNode.tagName.toLowerCase() == "p") {
            var mj = bM.previousSibling;
            var nM = parseFloat(bM.style.left);
            var mv = parseFloat(mj.style.left);
            if (nM < mv + mj.scrollWidth) {
                bM.style.left = (mv + mj.scrollWidth) + "px";
            }
        }
    }
    if (self.frameElement && self.frameElement.tagName == "IFRAME") {
        setTimeout(mi, 500);
    }
};
function gi() {
    var cJ = document.getElementsByTagName("video");
    var count = 0;
    for (var i = 0; i < cJ.length; i++) {
        try {
            if (mY) {
                count++;
            } else {
                if (cJ[i].readyState >= 1 || cJ[i].networkState == 3) count++;
            }
        } catch(e) {}
    }
    if (count >= cJ.length) {
        lO();
    } else {
        setTimeout(gi, 2000);
    }
};
function mi() {
    var eQ = document.createElement('iframe');
    var it = '/iframec.php#';
    var fT = aP;
    eQ.src = it + (fT + hF) + "#" + aS;
    eQ.style.display = 'block';
    eQ.width = "0";
    eQ.style.display = "none";
    document.body.appendChild(eQ);
};
function iH(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]);
    return null;
};
function lD() {
    if (addevt == 1) {
        document.removeEventListener("touchstart", fL, false);
        document.removeEventListener("touchmove", fK, false);
        document.removeEventListener("touchend", fl, false);
        var hpc = document.getElementsByClassName("hpc");
        for (var i = 0; i < hpc.length; i++) {
            hpc[i].removeEventListener("touchend", gw, false);
        }
        addevt = 0;
    } else if (addevt == 2) {
        document.removeEventListener("touchstart", fR, false);
        document.removeEventListener("touchend", gn, false);
        addevt = 0;
    }
};
function mb() {
    if (fm) {
        if (addevt == 1) return;
        lD();
        document.addEventListener("touchstart", fL, false);
        document.addEventListener("touchmove", fK, false);
        document.addEventListener("touchend", fl, false);
        var hpc = document.getElementsByClassName("hpc");
        for (var i = 0; i < hpc.length; i++) {
            var gw = hpc[i].onmouseup;
            hpc[i].addEventListener("touchend", gw, false);
            hpc[i].onmouseup = null;
        }
        addevt = 1;
    } else {
        document.onmousedown = fL;
        document.onmousemove = fK;
        document.onmouseup = fl;
        var ctl = document.getElementById("vcontrol");
        if (ctl) ctl.style.display = "none";
    }
};
document.onkeyup = function(e) {
    var x;
    if (event.which) {
        x = event.which;
    } else if (event.keyCode) {
        x = event.keyCode;
    }
    switch (x) {
    case 13:
        break;
    case 33:
    case 37:
        eG = 0;
        if (K.tl && K.tl["sp-1"] && K.tl["sp-1"].ak != 0) {
            K.tl["sp-1"].gO();
        } else {
            bE(aJ.ak - 1);
        }
        break;
    case 38:
        if (aJ.ak - 1 < 0) return;
        eG = 0;
        eI(K, aJ.ak);
        if (aJ.ak >= 0) {
            bE(aJ.ak - 1);
        }
        break;
    case 34:
    case 39:
        if (!K.tl || !K.tl["sp-1"] || K.tl["sp-1"].ak == K.tl["sp-1"].au.length) {
            bE(aJ.ak + 1);
        } else {
            K.tl["sp-1"].dG(K.tl["sp-1"]);
        }
        break;
    case 40:
        if (aJ.ak >= window._control.length - 1) return;
        bE(aJ.ak + 1);
        break;
    }
};
function initluke() {
    window.luke_html();
};
function BeginShow() {
    aJ.style.visibility = "";
    gZ = document.getElementById("s0");
    var loading = document.getElementById("loading");
    loading.style.display = "none";
    var rec = iH("rec");
    if (rec >= 1 && rec < 50) {
        mh(dir);
    }
    if (window.paint) {
        window.paint.init(aS, aP);
        mA = PaintReset;
    }
    if (rec == 50 && iscef) {
        $('body').append(cefsharp.spath("1.0"));
        setTimeout(initluke, 20);
        mb();
        cn = new Date().getTime();
        dU = "s_0_" + cn + ";";
        gI();
    } else if (rec == 100) {
        autoplay = 2;
    } else {
        if (window.optjson && window.optjson.wtoption & 0x04) {
            autoplay = 1;
            ShowCoverPage();
            lX();
        } else {
            mb();
            cn = new Date().getTime();
            dU = "s_0_" + cn + ";";
            gI();
        }
    }
};
function dX(a) {
    if (a.o && a.o.fX) {
        a.o = a.o.bp;
    }
    if (a.eff < 54 && a.eff != -2) {
        a.o.style.visibility = a.exit == 0 ? "hidden": "";
    }
    if (a.repeat > 1) a.dC = a.repeat;
    if (ereset["e" + a.eff]) ereset["e" + a.eff](a.o, a);
};
function dz(F) {
    if (F.bj != -1) {
        F.bu();
        F.bj = -1;
    }
    F.ak = 0;
    for (var ai = F.au.length - 1; ai >= 0; ai--) {
        var a = F.au[ai];
        if (a.aA) {
            for (var J = 0,
            gK = a.aA.length; J < gK; J++) {
                var c = a.aA[J];
                dX(c);
            }
        } else {
            dX(a);
        }
    }
};
function eI(d, fA) {
    if (_control[fA].animations == undefined) return;
    var fW = -2;
    for (var i = _control[fA].animations.length - 1; i >= 0; i--) {
        if (fW != _control[fA].animations[i].trigger) fW = _control[fA].animations[i].trigger;
        else continue;
        dz(d.tl["sp" + fW]);
    }
};
function jW(F) {
    for (var ai = F.au.length - 1; ai >= 0; ai--) {
        var a = F.au[ai];
        if (a.gap > 0) {
            if (a.aA && a.aA.length > 0) continue;
            if (a.hI) a.o.style.cssText = a.hI;
            a.o.style.visibility = "";
            hk(a.o, a);
        }
    }
};
function mU() {
    for (var ek = 0; ek < _control.length; ek++) {
        var d = document.getElementById("s" + ek);
        var vdis = 0;
        if (d.style.display == "none") {
            d.style.visibility = "hidden";
            d.style.display = "";
            vdis = 1;
        }
        for (var s in d.tl) {
            jW(d.tl[s]);
        }
        if (vdis == 1) {
            d.style.visibility = "";
            d.style.display = "none";
        }
    }
};
function ep(a, i) {
    if (a[i].eff >= 54 && a[i].eff <= 101) {}
    a[i].o.style.visibility = a[i].eff < 54 ? (a[i].exit ? "": "hidden") : "";
};
function fU(F) {
    return function() {
        F.et();
    }
};
function fY(aY) {
    var F = aY.target;
    F.pause();
    F.currentTime = 0;
    for (var i = aE.length - 1; i >= 0; i--) {
        if (aE[i].o == F.parentNode) {
            if (aE[i].lp2end == 1) {
                F.play();
            } else {
                aE.splice(i, 1);
            }
        }
    }
};
function gT(b) {
    if (b.hC == 0) {
        if (b.eff != 102) b.o.style.visibility = "";
        eC["e" + b.eff](b.o, b);
        b.hC = 1;
    }
    ay["e" + b.eff](b.o, b, 1 - b.exit);
    b.o.style.display = "none";
    b.o.offsetHeight;
    b.o.style.display = "";
    if (b.bp && !b.o.fX) {
        if (b.o.firstElementChild) b.o.firstElementChild.style.visibility = "";
        if (b.bp.parentNode) {
            b.bp.style.visibility = "hidden";
        }
    }
    if (b.eff < 54 && b.eff != 11 || b.eff == 62) {
        b.o.style.visibility = (b.exit ? "hidden": "");
        aj.Opacity(b.o, 1);
        if (b.o.style.visibility == "hidden") {
            if (b.dx !== undefined) b.o.style.left = b.dx + "px";
            if (b.dy !== undefined) b.o.style.top = b.dy + "px";
        }
    } else if (b.eff == 101 && b.ar == 1) {
        b.o.style.top = b.by + "px";
        b.o.style.left = b.aq + "px";
    } else if (b.ar == 1) {
        ay["e" + b.eff](b.o, b, 0);
    }
    ni(b);
};
function ia() {
    for (var i = 0; i < dV.length; i++) {
        var a = dV[i];
        if (a.gap > 0) {
            for (var i = 0,
            b = a.parent.aA,
            ai = b.length; i < ai; i++) {
                b[i].o.style.visibility = "hidden";
            }
        } else {
            a.o.style.visibility = "hidden";
        }
    }
    dV = [];
};
function ni(a) {
    switch (a.after) {
    case 2:
        if (a.gap > 0) {
            for (var i = 0,
            b = a.parent.aA,
            ai = b.length; i < ai; i++) {
                b[i].o.style.visibility = "hidden";
            }
        } else {
            a.o.style.visibility = "hidden";
        }
        break;
    case 3:
        dV.push(a);
        break;
    }
};
function mI() {
    for (var i = 0; i < cT.length; i++) {
        gT(cT[i]);
    }
    cT.splice(0);
};
function lQ(rl, i, bw, ef) {
    var b = rl[i];
    ay["e" + b.eff](b.o, b, 1 - b.exit);
    b.o.style.display = "none";
    b.o.offsetHeight;
    b.o.style.display = "";
    if (b.ar == 1) {
        if (b.exit == 0) {
            b.exit = 1;
            b.bu = bw;
            return 0;
        } else {
            b.exit = 0;
        }
    }
    if (b.repeat > 1000) {
        cT.push(b);
    } else if (b.repeat > 1 && b.repeat < 1000 && b.dC > 1) {
        ef.ak--;
        if (b.parent != null) ef.dI(b.parent);
        else ef.dI(b);
        b.dC--;
        b.bu = bw;
        return 0;
    }
    if (b.bp && !b.o.fX) {
        if (b.o.firstElementChild) b.o.firstElementChild.style.visibility = "";
        b.bp.style.visibility = "hidden";
    }
    if (b.eff < 54 && b.eff != 11 || b.eff == 62) {
        b.o.style.visibility = (b.exit ? "hidden": "");
        aj.Opacity(b.o, 1);
    }
    ni(b);
    if (b.o.style.visibility == "hidden") {
        if (b.dx !== undefined) b.o.style.left = b.dx + "px";
        if (b.dy !== undefined) b.o.style.top = b.dy + "px";
    }
    rl.splice(i, 1);
    return - 1;
};
function jH(rl, i, bw, ef) {
    var b = rl[i];
    b.bu = bw;
    if (b.ar == 1) {
        if (b.exit == 0) {
            b.exit = 1;
            return 0;
        } else {
            b.exit = 0;
        }
    }
};
function jq(rl, i, bw, ef, ea) {
    var b = rl[i];
    if (b == null) {
        return;
    }
    var r = (bw - b.bu);
    if (r < b.delay * 1000) {
        return 0;
    }
    if (b.hC == 0) {
        if (b.eff != 102) b.o.style.visibility = "";
        eC["e" + b.eff](b.o, b);
        ay["e" + b.eff](b.o, b, b.exit);
        b.hC = 1;
        if (b.snd != null) {
            b.snd.play();
        }
    }
    if (r > (b.duration + b.delay) * 1000) {
        return ea(rl, i, bw, ef);
    }
    r = (r / 1000 - b.delay) / b.duration;
    ay["e" + b.eff](b.o, b, b.exit ? 1 - r: r);
    return 0;
};
function hu(d, az) {
    d.tl = {};
    if (_control[az].animations) {
        var last = -10000;
        for (var i = 0; i < _control[az].animations.length; i++) {
            var a = _control[az].animations[i];
            if (a.trigger == null) a.trigger = -1;
            if (!a.exit) a.exit = 0;
            if (a.repeat == null) a.repeat = 1;
            if (a.after == null) a.after = 0;
            if (!a.delay) a.delay = 0;
            if (!a.type) a.type = 0;
            if (a.para == null) {
                a.para = -3;
            }
            if (a.trigger != last) {
                tt = {
                    au: []
                };
                d.tl["sp" + a.trigger] = tt;
                tt.trigger = a.trigger;
                tt.rl = [];
                tt.bj = -1;
                tt.gp = function() {
                    tt.bu();
                    tt.ak--;
                    ep(tt[tt.ak]);
                    var bZ = 1;
                    while (bZ && tt.ak > 0) {
                        switch (tt[tt.ak].type) {
                        case 1:
                        case 2:
                            tt.ak--;
                            ep(tt[tt.ak]);
                            break;
                        case 3:
                            bZ = 0;
                            break;
                        }
                    }
                };
                tt.bu = function() {
                    var now = (new Date().getTime()) - cn;
                    dU += "st_" + now + "_" + this.trigger + ";";
                    clearTimeout(this.bj);
                    this.bj = -1;
                    bN();
                    aG = null;
                    for (var i = 0; i < this.rl.length; i++) {
                        var b = this.rl[i];
                        gT(b);
                    }
                    while (this.ak < this.au.length && this.au[this.ak].type != 0) {
                        var c = this.au[this.ak];
                        if (c.eff >= 83 && c.eff <= 85 && c.o.dw) {
                            if (c.o.dw == 1) {
                                var F = c.o.getElementsByTagName("audio");
                            } else {
                                F = c.o.getElementsByTagName("video");
                            }
                            if (F.length > 0) {
                                if (c.eff == 83) {} else {
                                    if (!F[0].paused) {
                                        F[0].pause();
                                    }
                                }
                                F[0].loop = false;
                                F[0].addEventListener('ended', fY);
                                aE.push(c);
                            }
                            this.ak++;
                            continue;
                        }
                        if (c.aA) {
                            for (var i = 0,
                            ai = c.aA.length; i < ai; i++) {
                                var d = c.aA[i];
                                ay["e" + d.eff](d.o, d, 1 - d.exit);
                                if (d.bp && d.bp.parentNode) {
                                    d.o.firstElementChild.style.visibility = "";
                                    d.bp.style.visibility = "hidden";
                                }
                                d.o.style.visibility = (d.exit ? "hidden": "");
                            }
                        } else {
                            if (c.ar == 1) {
                                ay["e" + c.eff](c.o, c, 0);
                            } else {
                                eC["e" + c.eff](c.o, c, 1 - c.exit);
                                ay["e" + c.eff](c.o, c, 1 - c.exit);
                                if (c.bp && c.bp.parentNode) {
                                    c.o.firstElementChild.style.visibility = "";
                                    c.bp.style.visibility = "hidden";
                                }
                                c.o.style.visibility = (c.exit ? "hidden": "");
                            }
                        }
                        this.ak++;
                    }
                    this.rl = [];
                };
                tt.dI = function(b) {
                    console.log("addani" + b.s + " " + b.para + " " + b.eff);
                    console.log("curanim " + this.ak + " " + K.id + " " + this.au[0].trigger);
                    iS = K.id + "_" + this.ak + "_" + this.au[0].trigger;
                    var page = K.id;
                    var anim = this.ak;
                    var trigger = this.au[0].trigger;
                    page = page.substr(1);
                    var now = (new Date().getTime()) - cn;
                    dU += "a_" + now + "_" + iS + ";";
                    syncPageAnim(page, anim, trigger, false, now);
                    if (b.o && b.o.style.width == "0px") {
                        b.o.style.width = b.o.scrollWidth + "px";
                        b.o.style.height = b.o.scrollHeight + "px";
                    }
                    if (b.eff >= 83 && b.eff <= 85) {
                        if (b.o.dw == 1) {
                            var F = b.o.getElementsByTagName("audio");
                        } else {
                            F = b.o.getElementsByTagName("video");
                        }
                        if (F.length > 0) {
                            if (b.eff == 83) {
                                if (F[0].paused && !isNaN(F[0].duration)) {
                                    bN();
                                    F[0].play();
                                    aG = F[0];
                                }
                            } else {
                                if (!F[0].paused) {
                                    F[0].pause();
                                }
                            }
                            F[0].loop = false;
                            F[0].addEventListener('ended', fY);
                            aE.push(b);
                        }
                        this.ak++;
                        return;
                    }
                    if (b.aA) {
                        for (var J = 0,
                        ai = b.aA.length; J < ai; J++) {
                            var c = b.aA[J];
                            this.rl.push(c);
                            c.o.style.visibility = "";
                            eC["e" + c.eff](c.o, c);
                            ay["e" + c.eff](c.o, c, c.exit);
                            var bw = new Date().getTime();
                            c.bu = bw;
                        }
                    } else {
                        if (b.gap > 0 && !b.hI) {
                            b.hI = b.o.style.cssText;
                        }
                        this.rl.push(b);
                        b.hC = 0;
                        var bw = new Date().getTime();
                        b.bu = bw;
                    }
                    this.ak++;
                };
                tt.ex = function() {
                    if (this.ak < this.au.length) {
                        var b = this.au[this.ak];
                        this.dI(b);
                        var bZ = 1;
                        while (this.ak < this.au.length && this.au[this.ak].type == 1) {
                            b = this.au[this.ak];
                            this.dI(b);
                        }
                        if (this.bj == -1) {
                            this.bj = 1;
                            this.et();
                        }
                    } else if (this.au[0].trigger == -1) {
                        if (_control[aJ.ak].advt >= 0) {
                            es();
                        }
                    } else if (this.ak == this.au.length) {
                        dz(this);
                        if (this.au[0].type != 0) {
                            this.ex();
                        }
                    }
                };
                tt.et = function() {
                    var bw = new Date().getTime();
                    var rl = this.rl;
                    for (var i = 0; i < cT.length; i++) {
                        jq(cT, i, bw, this, jH);
                    }
                    for (var i = 0; i < rl.length; i++) {
                        i += jq(rl, i, bw, this, lQ);
                    }
                    if (rl.length == 0) {
                        if (this.ak < this.au.length && this.au[this.ak].type == 2 && autoplay == 0) {
                            this.ex();
                        } else {
                            this.bj = -1;
                            if (this.trigger == -1 && this.ak == this.au.length && _control[aJ.ak].advt >= 0 && autoplay == 0) {
                                es();
                            }
                            return;
                        }
                    }
                    window.clearTimeout(this.bj);
                    this.bj = setTimeout(fU(this), dA);
                };
                tt.gO = function() {
                    var now = (new Date().getTime()) - cn;
                    dU += "bk_" + now + "_" + this.trigger + "_" + K.id + ";";
                    this.bu();
                    do {
                        if (this.ak == 0) {
                            return;
                        }
                        this.ak--;
                        var a = this.au[this.ak];
                        if (a.aA) {
                            for (var J = 0,
                            gK = a.aA.length; J < gK; J++) {
                                var c = a.aA[J];
                                dX(c);
                            }
                        } else {
                            dX(a);
                        }
                    } while ( this . ak > 0 && this . au [ this . ak ].type != 0);
                    var page = K.id;
                    var anim = this.ak;
                    page = page.substr(1);
                    syncPageAnim(page, anim, this.trigger, true, now);
                };
                tt.dG = function() {
                    var now = (new Date().getTime()) - cn;
                    dU += "ra_" + now + "_" + this.trigger + "_" + K.id + ";";
                    var rl = this.rl;
                    if (rl.length != 0) {
                        this.bu();
                    }
                    this.ex();
                };
                tt.oh = function() {
                    if (this.ak == this.au.length) {
                        dz(this);
                    }
                };
                last = a.trigger;
            }
            var aN = document.getElementById("sp" + az + "#" + a.s);
            var o;
            o = fC(aN, a);
            if (!o) {
                continue;;
            }
            if (o.id && o.id.indexOf("#") >= 0) o.style["transform-origin"] = "center";
            o.style.width = o.scrollWidth + "px";
            o.style.height = o.scrollHeight + "px";
            if (eC['e' + a.eff] == window.af) {}
            if (ay["e" + a.eff] == null && a.eff < 82) {
                a.eff = 1;
            } else if (a.eff == 102) {
                o.fX = 1;
                if (!o.bp) {
                    var eo = document.createElement("canvas");
                    o.parentNode.insertBefore(eo, aN);
                    eo.width = aS;
                    eo.height = aP;
                    eo.style.position = "absolute";
                    bf = eo.getContext("2d");
                    if (o.style["background-color"]) {
                        a.fill = bf.fillStyle = o.style["background-color"];
                    }
                    if (aN.style["border-color"]) {
                        bf.strokeStyle = aN.style["border-color"];
                    } else if (o.style["background-color"]) {
                        bf.strokeStyle = aN.style["background-color"];
                    }
                    o.bp = eo;
                    o.bf = bf;
                }
                a.bp = o.bp;
                a.bf = o.bf;
                if (o.style["background-color"]) {
                    a.fill = o.style["background-color"];
                }
                a.aq = (o.offsetLeft + o.offsetWidth / 2);
                a.by = (o.offsetTop + o.offsetHeight / 2);
            }
            if (a.gap > 0) {
                var first = o.firstElementChild;
                if (!first) {
                    a.gap = 0;
                } else if (first.className == 'ib') {
                    var p = {};
                    for (var am in a) {
                        p[am] = a[am];
                    }
                    _control[az].animations.splice(i, 0, p);
                    tt.au.push(p);
                    p.o = first;
                    a.type = 2;
                    p.snd = null;
                    p.para = -2;
                    a.delay = 0;
                    p.repeat = 1;
                    p.after = 0;
                    p.gap = 0;
                    p.aA = null;
                    i++;
                }
            }
            a.o = o;
            if (a.snd != null) {
                var cu = document.createElement("audio");
                cu.src = a.snd;
                cu.preload = "auto";
                if (a.o) a.o.appendChild(cu);
                a.snd = cu;
            }
            tt.au.push(a);
        }
    }
};
function eP(o, a) {
    var src = o.getElementsByTagName("p");
    for (var v = 0; v < src.length; v++) {
        var gr = src[v];
        if (gr.children[0] && gr.children[0].className == "vchar") {
            gr = gr.children[0];
        }
        for (var F = 0,
        gH = gr.children.length; F < gH; F++) {
            var T = "";
            var child = gr.children[F];
            if (child.children.length > 0) {
                child = child.children[0];
            }
            var Text = child.innerHTML.replace(/<.+?>/gim, '');
            if (a.by == 1) {
                var au = Text.split(" ");
                for (var i = 0,
                hE = au.length; i < hE; i++) {
                    T += "<span class=\"sub1\" style=\"white-space:inherit\">" + au[i] + " </span>";
                }
            } else {
                for (var i = 0,
                hE = Text.length; i < hE; i++) {
                    if (Text[i] == '&') {
                        var pos = Text.indexOf(';', i);
                        T += "<span class=\"sub1\" style=\"white-space:inherit\">" + Text.substr(i, pos - i + 1) + "</span>";
                        i = pos;
                    } else {
                        T += "<span class=\"sub1\" style=\"white-space:inherit\">" + Text[i] + "</span>";
                    }
                }
            }
            child.innerHTML = T;
            var aa = child.getElementsByClassName("sub1");
            T = "";
            if (iF) {
                var of = 0;
                for (var i = 0; i < aa.length; i++) {
                    T += "<span class=\"sub\" style=\"white-space:inherit;left:" + of + "px;top:" + aa[i].offsetTop + "px;\">" + aa[i].innerHTML + "</span>";
                    of += aa[i].offsetWidth;
                }
            } else {
                for (var i = 0; i < aa.length; i++) {
                    T += "<span class=\"sub\" style=\"white-space:inherit;left:" + aa[i].offsetLeft + "px;top:" + aa[i].offsetTop + "px;\">" + aa[i].innerHTML + "</span>";
                }
            }
            child.innerHTML = T;
            gr.children[F].style.width = "1em";
        }
    }
};
function hk(o, a) {
    var bX = o.getElementsByClassName("sub");
    if (bX.length != 0) {} else {
        eP(o, a);
        a.o = null;
        bX = o.getElementsByClassName("sub");
        if (bX.length == 0) {
            a.o = o;
            a.gap = 0;
            return;
        }
    }
    a.aA = [];
    for (var i = 0,
    ai = bX.length; i < ai; i++) {
        var p = {};
        for (var am in a) {
            p[am] = a[am];
        }
        p.o = bX[i];
        p.type = 1;
        p.snd = null;
        p.delay = a.delay + i * a.gap;
        p.repeat = 1;
        p.after = 0;
        p.aA = null;
        a.aA.push(p);
        dX(p);
    }
    a.aA[0].snd = a.snd;
    a.aA[a.aA.length - 1].type = a.type;
    a.aA[a.aA.length - 1].after = a.after;
    a.aA[a.aA.length - 1].parent = a;
    a.aA[a.aA.length - 1].dC = a.aA[a.aA.length - 1].repeat = a.repeat;
    a.aA[a.aA.length - 1].gap = a.gap;
};
function gx(pos, F) {
    var fD = window.pageYOffset;
    var bu = new Date().getTime();
    var f = function() {
        var bw = new Date().getTime();
        var dY = fD + (pos - fD) * (bw - bu) / F;
        window.scrollTo(0, dY);
        if (bw < bu + F) {
            clearTimeout(cF);
            cF = setTimeout(f, dA);
        } else cF = -1;
    };
    clearTimeout(cF);
    cF = setTimeout(f, dA);
};
function fj() {
    var zoom;
    var root = document.getElementById("root");
    if (mW > 0 && !iscef) {
        aW = document.documentElement.clientHeight;
        bV = document.documentElement.clientWidth;
        if ((aW - bV) * (aP - aS) < 0) {
            var bG = aW;
            aW = bV;
            bV = bG;
            root.style[hx] = "rotate(90deg)";
            root.style[gf] = "0 0";
            root.style.left = aW + "px";
            eY = 1;
        } else {
            root.style[hx] = "";
            root.style.left = "0px";
            eY = 0;
        }
    } else {
        root.style[hx] = "";
        root.style.left = "0px";
        eY = 0;
        aW = document.documentElement.clientHeight;
        bV = document.documentElement.clientWidth;
    }
    root.style.width = bV + "px";
    root.style.height = aW + "px";
    root.style.overflow = "hidden";
    if (navigator.userAgent.toLowerCase().indexOf("firefox") < 0) {
        aJ.style[hx] = "scale(0.25)";
        aJ.style[gf] = "0 0";
    }
    if (aW / bV > aP / aS) {
        dv = (aW - aP * bV / aS) / 2;
        dK = 0;
        zoom = bV / aS;
        dv /= zoom;
    } else {
        dv = 0;
        dK = (bV - aS * aW / aP) / 2;
        zoom = aW / aP;
        dK /= zoom;
    }
    var lV = navigator.userAgent.toLowerCase();
    if (lV.indexOf("msie 9.0") >= 0) {
        ew = 1;
    } else {
        ew = zoom;
    }
    ew *= 4;
    aJ.style.zoom = ew;
    aJ.style.top = dv / 4 + "px";
    aJ.style.left = dK / 4 + "px";
    var d = document.getElementById("s_cvr1");
    if (d) {
        var zm1 = bV / 720;
        if (aW / bV > 0.75) {
            zm1 = bV / 720;
            d.style.top = (aW / zm1 - 540) / 2 + "px";
            d.style.left = "0px";
        } else {
            zm1 = aW / 540;
            d.style.left = (bV / zm1 - 720) / 2 + "px";
            d.style.top = "0px";
        }
        d.style.zoom = zm1;
    }
    if (window.paint) {
        window.paint.fj(ew / 4, eY, aS, aP);
    }
};
var css = function(hN, cO) {
    var key, fQ;
    for (key in cO) {
        if (cO.hasOwnProperty(key)) {
            fQ = hM(key);
            if (fQ !== null) {
                hN.style[fQ] = cO[key];
            }
        }
    }
    return hN;
};
var hM = (function() {
    var style = document.createElement('dummy').style,
    iB = '-webkit- -moz- -o- -ms-'.split(' '),
    gW = {};
    return function(am) {
        if (typeof gW[am] === "undefined") {
            var iV = am.charAt(0).toLowerCase() + am.substr(1),
            cO = (am + ' ' + iB.join(iV + ' ') + iV).split(' ');
            gW[am] = null;
            for (var i in cO) {
                if (style[cO[i]] !== undefined) {
                    gW[am] = cO[i];
                    break;
                }
            }
        }
        return gW[am];
    };
})();
window.onresize = function() {
    if (!aJ) aJ = document.getElementById("main");
    if (!aJ) return;
    fj();
};
function eW(e) {
    var x = y = 0;
    if (!e) e = window.event;
    var al = 0;
    if (e.type == "touchstart") {
        e = e.touches[0];
    } else if (e.type == "touchend") {
        e = e.changedTouches[0];
    }
    if (e.screenX && e.screenY) {
        x = e.screenX;
        y = e.screenY;
    } else if (e.clientX && e.clientY) {
        x = e.clientX;
        y = e.clientY;
    }
    return {
        'x': x,
        'y': y
    };
};
var lastTimestamp = 0;
var timeLimit = 0;
function FreqActPrevent() {
    var nowStamp = Date.parse(new Date());
    if (timeLimit && nowStamp - lastTimestamp < timeLimit) {
        return 1;
    } else {
        lastTimestamp = nowStamp;
    }
    return 0;
}
function fL(aY) {
    if (aY && aY.target && aY.target.tagName.toLowerCase() == "embed") return;
    if (FreqActPrevent()) return;
    if (!fm) aY.preventDefault();
    if (window.isserver == 1) {
        SeverHandleDown(aY);
    }
    if (aY.type == "touchstart") {
        //if (aY.touches.length > 1) {
        //   cN = 0;
        //    return;
        //}
    }
    cN = 1;
    bc = eW(aY);
};
function fK() {};
function fl(aY) {
    if (aY.button & 0x02) {
        if (window.DoMenu) window.DoMenu( - 1);
        cY = 0;
        return;
    }
    if (autoplay > 0) return;
    window.clearTimeout(fI);
    if (cN == 0) return;
    cN = 0;
    var now = (new Date().getTime()) - cn;
    dU += "c_" + now + ";";
    if (dV.length) {
        ia();
    }
    for (var i = 0; i < cT.length; i++) {
        if (cT[i].repeat < 0x20000) {
            gT(cT[i]);
        }
    }
    if (eG == 1) {
        var now = (new Date().getTime()) - cn;
        dU += "r_" + now + ";";
        if (is_replay == 0) return;
        eG = 0;
        mF();
        return;
    }
    if (cY == 1) {
        cY = 0;
        return;
    }
    var gQ = eW(aY);
    if (!iscef) {
        var bP = -10000;
        if (eY) {
            if (bc.y - gQ.y < -50) {
                bP = aJ.ak - 1;
            } else if (bc.y - gQ.y > 50) {
                bP = aJ.ak + 1;
            }
        } else {
            if (bc.x - gQ.x > 50) {
                bP = aJ.ak + 1;
            } else if (bc.x - gQ.x < -50) {
                bP = aJ.ak - 1;
            }
        }
        if (bP != -10000) {
            if (bP >= 0) {
                if (fm && autoplay > 0) jG(bP - aJ.ak);
                else bE(bP);
            }
            return;
        }
    }
    lW();
    var as = cX(aY);
    var cP;
    if (as.id && as.id.indexOf("sp" + aJ.ak + "#") >= 0) {
        var pos = as.id.indexOf("#");
        var a = K.tl["sp" + as.id.substr(pos + 1)];
        if (a) {
            a.dG(a);
            return;
        }
    }
    if (K.tl && K.tl["sp-1"]) {
        if (K.tl["sp-1"].bj != -1) {
            K.tl["sp-1"].bu();
            return;
        }
        if (K.tl["sp-1"]) {
            if (K.tl["sp-1"].ak == K.tl["sp-1"].au.length) {
                if ((window._control[aJ.ak].advc != 1 || window._control[aJ.ak].advt != null)) {
                    bE(aJ.ak + 1);
                }
            } else {
                K.tl["sp-1"].dG(K.tl["sp-1"]);
            }
        }
    } else {
        if ((window._control[aJ.ak].advc != 1 || window._control[aJ.ak].advt != null)) {
            if (window.toNext) {
                window.toNext();
            } else {
                bE(aJ.ak + 1)
            }
        }
    }
};
function lW() {
    if (ag != -1) {
        var dB = document.getElementById("s" + be);
        var bz = document.getElementById("s" + aJ.ak);
        var now = (new Date().getTime()) - cn;
        dU += "sp_" + now + "_" + be + "_" + aJ.ak + ";";
        window.clearTimeout(ag);
        ag = -1;
        if (window._control[aJ.ak].trans) {
            var p = window._control[aJ.ak].trans;
            if (bh["e" + p.eff]) bh["e" + p.eff](dB, bz, p, 1);
            if (hh["e" + p.eff]) hh["e" + p.eff](dB, bz, p);
        }
        fp(dB, bz);
        return;
    }
};
function jr(name, value, mO) {
    var lC = mO;
    var exp = new Date();
    exp.setTime(exp.getTime() + lC * 24 * 60 * 60 * 1000);
    document.cookie = name + "=" + escape(value) + ";expires=" + exp.toGMTString();
};
function mn(name) {
    var exp = new Date();
    exp.setTime(exp.getTime() - 1);
    var iJ = hW(name);
    if (iJ != null) {
        document.cookie = name + "=" + iJ + ";expires=" + exp.toGMTString();
    }
};
function hW(name) {
    var au, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
    if (au = document.cookie.match(reg)) {
        return unescape(au[2]);
    } else {
        return null;
    }
};
function iw(fg) {
    if (document.addEventListener) {
        document.addEventListener('DOMContentLoaded',
        function() {
            document.removeEventListener('DOMContentLoaded', arguments.callee, false);
            fg();
        },
        false);
    } else if (document.attachEvent) {
        document.attachEvent('onreadystatechange',
        function() {
            if (document.readyState == 'complete') {
                document.detachEvent('onreadystatechange', arguments.callee);
                fg();
            }
        });
    }
};
window.onload = function() {
    var flag = document.getElementById("loading").style.display;
    if (flag == "none") {
        document.getElementById("loading").style.display = "";
        var hO = aJ.style.top;
        aJ.style.top = "2000px";
    }
    for (var hn = 0; hn < _note.length; hn++) {
        document.getElementById("s" + hn).style.display = "";
    }
    kN();
    if (K && K.tl && K.tl["sp-1"] && K.tl["sp-1"].bj != -1) {
        K.tl["sp-1"].bu();
    }
    mU();
    for (var hn = 0; hn < _note.length; hn++) {
        document.getElementById("s" + hn).style.display = "none";
    }
    if (flag == "none") {
        if (K) K.style.display = "";
        document.getElementById("loading").style.display = "none";
        aJ.style.top = hO;
    }
    if (window.thum) {
        var arrows = document.createElement("div");
        arrows.innerHTML = "<div class='leftarrow'><img id='butl' src='../left.png' onmouseup='onMenu(0)'/></div><div class='rightarrow'><img id='butr' src='../right.png' onmouseup='onMenu(1)'/></div>";
        document.body.appendChild(arrows);
        var gs = navigator.userAgent.toLowerCase();
        if (gs.indexOf("android") >= 0 || gs.indexOf("iphone") >= 0 || gs.indexOf("ipad") >= 0) {
            var but = document.getElementById("butl");
            but.addEventListener('touchend', but.onmouseup, false);
            but.onmouseup = null;
            but = document.getElementById("butr");
            but.addEventListener('touchend', but.onmouseup, false);
            but.onmouseup = null;
        }
        window.thum();
    }
};
iw(function() {
    try {
        var rec = iH("rec");
        if (typeof(cefsharp) != 'undefined') {
            iscef = 1;
            cefsharp.init();
        }
    } catch(e) {
        console.log(e);
    }
    var jA = navigator.userAgent.toLowerCase();
    if (jA.indexOf("android") >= 0 || jA.indexOf("iphone") >= 0 || jA.indexOf("ipad") >= 0) {
        hx = "-webkit-transform";
        gf = "-webkit-transform-origin"
    }
    jV();
});
function hQ(pos) {
    var hl = aW / 3;
    var ij = pos == null ? window.pageYOffset: pos;
    for (var i = 0; i < _note.length; i++) {
        var bP = document.getElementById("s" + i);
        var hL = bP.offsetTop * bP.style.zoom - ij;
        if (hL > hl) break;
    }
    i--;
    var bP = document.getElementById("s" + i);
    if (bP != K) {
        eI(bP, i);
        be = aJ.ak;
        aJ.ak = i;
        K = bP;
        if (pos == null) {
            eu(bP);
        }
    }
};
function oY() {
    var gv = navigator.userAgent.toLowerCase();
    var mk = gv.match(/ipad/i) == "ipad";
    var nu = gv.match(/iphone os/i) == "iphone os";
    var oS = gv.match(/android/i) == "android";
    var nD = gv.match(/MicroMessenger/i) == "micromessenger";
    var pq = gv.match(/windows mobile/i) == "windows mobile";
    iF = gv.toLowerCase().indexOf("edge") > 0;
    mz = mk || nu;
    fm = mz || oS;
    mY = mz && nD;
};
var xmlHttpRequest;
function jb(method, url, ea) {
    if (window.XMLHttpRequest) {
        xmlHttpRequest = new XMLHttpRequest();
        xmlHttpRequest.ea = ea;
        if (xmlHttpRequest.overrideMimeType) {
            xmlHttpRequest.overrideMimeType("text/xml");
        }
    }
    if (xmlHttpRequest) {
        xmlHttpRequest.onreadystatechange = lm;
        xmlHttpRequest.open(method, url, true);
        xmlHttpRequest.send(null);
    }
};
function lm() {
    if (xmlHttpRequest.readyState == 4) {
        if (xmlHttpRequest.status == 200) {
            xmlHttpRequest.ea(xmlHttpRequest.responseText);
        }
    }
};
function jw(ge) {
    if (ge == "" || ge[0] == '<') {
        var json = {
            read: 0,
            up: 0,
            vip: 0,
            wtoption: 2
        };
    } else {
        var json = ge;
    }
    mW = parseInt(json.wtoption) & 0x02;
    if (json.desc && json.desc.length > 2) {
        window.wxdesc = json.desc;
    } else {
        window.wxdesc = document.title;
    }
    if (json.vip < 9) {
        var d = document.createElement("div");
        d.className = "mode2";
        d.style.zoom = 1;
        d.innerHTML = "";
        document.getElementById("root").appendChild(d);
    }
};
function UpNum() {
    var iP = hW(window.firsttag + "zan");
    if (iP != null) return;
    var ah = document.getElementById("likebtn");
    if (ah && ah.className.indexOf("praised") < 0) {
        ah.className += " praised";
        var ah = document.getElementById("likenum");
        ah.innerHTML = window.optjson.up + 1;
    }
    var dd = document.getElementById("zan");
    if (dd) dd.style.backgroundImage = "url(../img/upred.png)";
    var iv = function(json) {};
    jb("Get", "/upnum.php?tag=" + window.firsttag, iv);
    jr(window.firsttag + "zan", 1, 365);
};
function oq() {
    if (window.frames.length != parent.frames.length) {
        gA = fS;
        gA("pgnumber=" + window._control.length);
    } else if (iscef) {
        gA = cefsharp.cef_gopage;
        gA("pgnumber=" + window._control.length);
    }
};
function jV() {
    if (window.optjson) {
        jw(window.optjson);
    }
    om();
    aJ = document.getElementById("main");
    aS = parseFloat(aJ.style.width);
    aP = parseFloat(aJ.style.height);
    aJ.style.top = "2000px";
    aJ.style.visibility = "";
    var loading = document.getElementById("loading");
    loading.style.display = "";
    for (var hn = 0; hn < _note.length; hn++) {
        ka(hn);
    }
    var F = document.getElementsByTagName("audio");
    for (i = 0; i < F.length; i++) {
        F[i].parentNode.dw = 1;
    }
    if (pptcolor != null) {
        document.body.style.background = pptcolor;
    }
    var cJ = document.getElementsByTagName("video");
    gd = cJ.length;
    if (gd > 0) {
        for (var i = 0; i < cJ.length; i++) {
            cJ[i].parentNode.dw = 2;
            cJ[i].md = "auto";
        }
        setTimeout(gi, 1000);
    } else {
        lO();
    }
    aJ.ak = 0;
    K = document.getElementById("s0");
    UpCount();
};
function UpCount() {
    var iP = hW(window.firsttag + "read");
    if (iP != null) return;
    jb("Get", "/getstat2.php?tag=" + window.firsttag,
    function(json) {});
    jr(window.firsttag + "read", 1, 365);
};
function lO(a) {
    aJ.style.visibility = "hidden";
    fj(); {
        var iP = hW(window.firsttag + "zan");
        if (iP != null) {
            var ah = document.getElementById("likebtn");
            if (ah && ah.className.indexOf("praised") < 0) {
                ah.className += " praised";
                var ah = document.getElementById("likenum");
                ah.innerHTML = window.optjson.up;
            }
            var dd = document.getElementById("zan");
            if (dd) dd.style.backgroundImage = "url(../img/upred.png)";
        }
    }
    if (iscef) {
        setTimeout(BeginShow, 500);
    } else setTimeout(BeginShow, 500);
};
function ik() {
    if (advcp == aJ.ak + 1) {
        bE(aJ.ak + 1);
    }
};
function es(aY) {
    if (_control[aJ.ak].advt && _control[aJ.ak].advt > 0) {
        window.clearTimeout(fI);
        fI = setTimeout(ik, _control[aJ.ak].advt * 1000);
        advcp = aJ.ak + 1;
    } else {
        bE(aJ.ak + 1);
    }
};
function cX(e) {
    if (!e) e = window.event;
    if (e.target) {
        aX = e.target;
    } else if (e.srcElement) {
        aX = e.srcElement;
    }
    if (aX.nodeType == 3) {
        aX = aX.parentNode;
    }
    while (aX.id == "") aX = aX.parentNode;
    return aX;
};
function playmedia(i) {
    var F;
    if (window.event) {
        as = cX(window.event);
        F = as.getElementsByTagName(i == 1 ? "audio": "video");
        if (F.length > 0) {
            if (F[0].paused && !isNaN(F[0].duration)) {
                bN();
                F[0].play();
                aG = F[0];
            } else {
                F[0].pause();
            }
        }
    }
    cY = 1;
};
function hp(a, ah) {
    if (ag >= 0) return;
    if (cY == 1) return;
    var now = (new Date().getTime()) - cn;
    dU += "hp_" + now + "_" + a + ";";
    cY = 1;
    if (a.indexOf("http") == 0 || a.indexOf("mailto") == 0) {
        if (iscef) {
            alert("当前环境不支持跳转到第三方页面");
            return;
        }
        if (mz) {
            location.href = a;
        } else {
            window.open(a);
        }
    } else if (a.indexOf("tel:") >= 0 || a.indexOf("sms:") >= 0) {
        if (iscef) {
            alert("当前环境不支持跳转到第三方页面");
            return;
        }
        window.open(a);
    } else if (a.indexOf("event:") == 0) {
        var gb = -1;
        a = a.substring(7);
        if (a[0] >= '0' && a[0] <= '9') {
            gb = parseInt(a);
        } else if (a[0] == 'p') {
            if (a == "ppEnd") {
                gb = _note.length - 1;
            } else if (a == "prev") {
                gb = aJ.ak - 1;
            } else {
                gb = be;
            }
        } else if (a == "next") {
            gb = aJ.ak + 1;
        }
        playFromPage(gb);
        var p = document.getElementById("s" + gb);
        eI(p, gb);
        mI();
        bE(gb);
    }
};
function kF(aN, V, p) {
    aN.style.zIndex = 1;
    V.style.zIndex = 99;
    gc(V);
    switch (p.dir) {
    case 1:
        V.style.top = aP + "px";
        break;
    case 0:
        V.style.left = aS + "px";
        break;
    case 2:
        V.style.left = -aS + "px";
        break;
    case 3:
        V.style.top = -aP + "px";
        break;
    }
};
function dQ(aN, V, p) {
    ed(V, p);
    V.style.display = "none";
    p.bp.style.display = "inline";
    p.cx = p.bp.width / 2;
    p.cy = p.bp.height / 2;
    p.radius = (p.bp.width > p.bp.height) ? p.bp.width * 0.8 : p.bp.height * 0.8;
    p.o = V;
    p.bp.style.zIndex = 99;
    gc(p.bp);
    aN.style.zIndex = 1;
};
function kM(aN, V, p) {
    aN.style.zIndex = 99;
    V.style.zIndex = 1;
    gc(aN);
};
function lz(aN, V, p) {
    switch (p.dir) {
    case 0:
        V.style.left = aS + "px";
        break;
    case 1:
        V.style.left = aS + "px";
        V.style.top = aP + "px";
        break;
    case 2:
        V.style.top = aP + "px";
        break;
    case 3:
        V.style.left = -aS + "px";
        V.style.top = aP + "px";
        break;
    case 4:
        V.style.left = -aS + "px";
        break;
    case 5:
        V.style.left = -aS + "px";
        V.style.top = -aP + "px";
        break;
    case 6:
        V.style.top = -aP + "px";
        break;
    case 7:
        V.style.left = aS + "px";
        V.style.top = -aP + "px";
        break;
    }
    V.style.zIndex = 99;
    aN.style.zIndex = 1;
    gc(V);
};
var hH = {
    e769: dQ,
    e1284: lz,
    e2049: kM,
    e3074: dQ,
    e3849: function(aN, V, p) {
        aN.style.filter = 'alpha(opacity=100)';
        V.style.filter = 'alpha(opacity=0)';
        aN.style.opacity = 1;
        V.style.opacity = 0;
    },
    e3850: function(aN, V, p) {
        V.style["transformOrigin"] = "center";
        V.style.zIndex = 99;
        gc(V);
    },
    e3853: kF,
    e2817: dQ,
    e3586: dQ,
    e3845: dQ,
    e3846: dQ,
    e3847: dQ,
    e3851: dQ,
    e3856: dQ,
    e3857: dQ,
    e2567: dQ,
    eff: function(aN, V, p) {}
};
var bh = {
    e1: function(aN, V, p, r) {},
    e2: function(aN, V, p, r) {},
    e257: function(aN, V, p, r) {},
    e258: function(aN, V, p, r) {},
    e1284: function(aN, V, p, r) {
        switch (p.dir) {
        case 0:
            V.style.left = aS * (1 - r) + "px";
            break;
        case 1:
            V.style.left = aS * (1 - r) + "px";
            V.style.top = aP * (1 - r) + "px";
            break;
        case 2:
            V.style.top = aP * (1 - r) + "px";
            break;
        case 3:
            V.style.left = ( - aS * (1 - r)) + "px";
            V.style.top = aP * (1 - r) + "px";
            break;
        case 4:
            V.style.left = -aS * (1 - r) + "px";
            break;
        case 5:
            V.style.left = ( - aS * (1 - r)) + "px";
            V.style.top = (aP * (1 - r)) + "px";
            break;
        case 6:
            V.style.top = ( - aP * (1 - r)) + "px";
            break;
        case 7:
            V.style.left = (aS * (1 - r)) + "px";
            V.style.top = ( - aP * (1 - r)) + "px";
            break;
        }
    },
    e1793: function(aN, V, p, r) {
        if (r > 0.5) {
            aN.style.opacity = 0;
            V.style.opacity = 2 * r - 1;
        } else {
            V.style.opacity = 0;
            aN.style.opacity = 1 - 2 * r;
        }
    },
    e2049: function(aN, V, p, r) {
        switch (p.dir) {
        case 0:
            aN.style.left = ( - aS * r) + "px";
            break;
        case 1:
            aN.style.left = ( - aS * r) + "px";
            aN.style.top = ( - aP * r) + "px";
            break;
        case 2:
            aN.style.top = ( - aP * r) + "px";
            break;
        case 3:
            aN.style.left = (aS * r) + "px";
            aN.style.top = ( - aP * r) + "px";
            break;
        case 4:
            aN.style.left = (aS * r) + "px";
            break;
        case 5:
            aN.style.left = (aS * r) + "px";
            aN.style.top = (aP * r) + "px";
            break;
        case 6:
            aN.style.top = (aP * r) + "px";
            break;
        case 7:
            aN.style.top = (aP * r) + "px";
            aN.style.left = ( - aS * r) + "px";
            break;
        }
    },
    e2567: function(aN, V, p, r) {},
    e3850: function(aN, V, p, r) {
        V.style.transform = "rotate(" + ( - 360 * r) + "deg) scale(" + r + "," + r + ")";
    },
    e3853: function(aN, V, p, r) {
        switch (p.dir) {
        case 1:
            V.style.top = (aP * (1 - r)) + "px";
            aN.style.top = ( - aP * r) + "px";
            break;
        case 3:
            V.style.top = ( - aP * (1 - r)) + "px";
            aN.style.top = (aP * r) + "px";
            break;
        case 0:
            V.style.left = (aS * (1 - r)) + "px";
            aN.style.left = ( - aS * r) + "px";
            break;
        case 2:
            V.style.left = ( - aS * (1 - r)) + "px";
            aN.style.left = (aS * r) + "px";
            break;
        }
    },
    e3849: function(aN, V, p, r) {
        if (r < 0.5) {
            aN.style.filter = 'alpha(opacity=' + ((1 - 2 * r) * 100) + ')';
            V.style.filter = 'alpha(opacity=0)';
            aN.style.opacity = 1 - 2 * r;
            V.style.opacity = 0;
        } else {
            V.style.opacity = 2 * r - 1;
            aN.style.opacity = 0;
            V.style.filter = 'alpha(opacity=' + ((2 * r - 1) * 100) + ')';
            aN.style.filter = 'alpha(opacity=0)';
        }
    },
    e10: function(aN, V, p, r) {},
    eff: function(aN, V, p, r) {
        aN.style.left = (p.an + (p.cw - p.an) * r) + "px";
        V.style.left = (p.cf + (p.cQ - p.cf) * r) + "px";
    }
};
function cd(aN, V, p) {
    if (p.bp) p.bp.style.display = "none";
};
var hh = {
    e769: cd,
    e2567: cd,
    e3074: cd,
    e3849: function(aN, V, p) {
        aN.style.filter = V.style.filter = 'alpha(opacity=100)';
        aN.style.opacity = V.style.opacity = 1;
    },
    e2817: cd,
    e3586: cd,
    e3845: cd,
    e3846: cd,
    e3847: cd,
    e3851: cd,
    e3856: cd,
    e3857: cd
};
function gM(aN, V) {
    aN.style.top = (parseInt(d.id.substring(1)) * aP) + "px";
};
function eu(V) {
    var bP = aJ.ak;
    for (var i = aE.length - 1; i >= 0; i--) {
        if (!aE[i].lp2end && (!aE[i].sas || (aE[i].sas && aE[i].sas < bP))) {
            if (aE[i].o.dw == 1) {
                var F = aE[i].o.getElementsByTagName("audio");
            } else {
                var F = aE[i].o.getElementsByTagName("video");
            }
            F[0].pause();
            F[0].currentTime = 0;
            aE.splice(i, 1);
        }
    }
    if (autoplay > 0) return;
    if (_control[bP].advt != null) {
        if (V.tl != null && V.tl["sp-1"]) V.tl["sp-1"].dG();
        else {
            window.clearTimeout(fI);
            fI = setTimeout(ik, _control[aJ.ak].advt * 1000);
            advcp = aJ.ak + 1;
        }
        return;
    }
    if (V.tl && V.tl["sp-1"] && V.tl["sp-1"].ak == 0 && V.tl["sp-1"].au[0] && V.tl["sp-1"].au[0].type != 0) {
        V.tl["sp-1"].dG()
    }
};
function eN(aN, V, p) {
    return function() {
        var bw = new Date().getTime();
        var r = (bw - p.bu) / p.duration / 1000;
        if (r >= 1) {
            bh["e" + p.eff](aN, V, p, 1);
            if (p.bp) {
                p.bp.style.visibility = "hidden";
            }
            if (hh["e" + p.eff]) hh["e" + p.eff](aN, V, p);
            fp(aN, V);
            ag = -1;
        } else {
            bh["e" + p.eff](aN, V, p, r);
            window.clearTimeout(ag);
            ag = setTimeout(eN(aN, V, p), 20);
        }
    }
};
function kg() {
    for (i = aE.length - 1; i >= 0; i--) {
        if (aE[i].o.dw == 2 && !aE[i].lp2end) {
            var F = aE[i].o.getElementsByTagName("video");
            if (F.length > 0) F[0].pause();
            aE.splice(i, 1);
        }
    }
};
function cG(aN, V, p) {
    kg();
    aN.style.display = "";
    V.style.display = "";
    if (bh["e" + p.eff] == null) {
        fp(aN, V);
        return;
    }
    if (hH["e" + p.eff]) hH["e" + p.eff](aN, V, p);
    p.bu = new Date().getTime();
    bh["e" + p.eff](aN, V, p, 0);
    window.clearTimeout(ag);
    ag = setTimeout(eN(aN, V, p), 20);
};
function bN() {
    if (aG) {
        aG.pause();
        aG.currentTime = 0;
    }
};
function fp(aN, V) {
    aN.style.display = "none";
    V.style.display = "";
    console.log("after " + aN.id + ":" + V.id);
    eu(V);
};
function gc(o) {
    if (gZ != o) gZ.style.zIndex = 0;
    gZ = o;
};
function fZ() {
    window.clearTimeout(dN);
    dN = null;
    if (hG != -1) {
        bE(hG);
        return;
    }
    eu(K);
};
function mF() {
    gI();
};
function bE(bP) {
    console.log("gopage " + bP);
    if (window.paintstate == 0) DoMenu(1);
    if (!window.noeff) {
        var now = (new Date().getTime()) - cn;
        syncPageAnim(bP, -1, -1, false, now);
    }
    if (aJ.ak == bP && bP != 0) {
        return;
    }
    mA(bP);
    lW();
    if (autoplay == 1) {
        document.getElementById("bpag").innerText = parseInt(bP) + 1;
    }
    mI();
    bN();
    var bO = document.getElementById("s" + bP);
    if (bO == null) {
        eG = 1;
        gA("end=1");
        if (window.lukestate && window.lukestate == 1) {
            alert("您已到达最后一页，点击鼠标将从头开始播放。停止录制请点击下方停止录制按钮。");
        } else if (autoplay) {} else {}
        return;
    }
    dU += "g_" + now + "_" + bP + ";";
    gA("pg=" + bP);
    var bz = document.getElementById("s" + aJ.ak);
    be = aJ.ak;
    aJ.ak = parseInt(bP);
    K = bO;
    var a = document.getElementById("a" + K.id.substr(1));
    if (a) {
        a.play();
        aG = a;
    }
    if (window._control[bP].trans != null && bz != bO && (!window.noeff)) {
        bO.style.left = "0px";
        bO.style.top = "0px";
        cG(bz, bO, window._control[bP].trans);
    } else {
        bO.style.top = "0px";
        bO.style.left = "0px";
        fp(bz, bO);
    }
};
function gI() {
    bN();
    for (var i = 0; i < aE.length; i++) {
        if (aE[i].o.dw == 1) {
            var F = aE[i].o.getElementsByTagName("audio");
        } else {
            var F = aE[i].o.getElementsByTagName("video");
        }
        if (F.length > 0 && !F[0].paused) {
            F[0].pause();
            F[0].currentTime = 0;
        }
    }
    aE.splice(0);
    for (var i = 0; i < _note.length; i++) {
        var p = document.getElementById("s" + i);
        p.style.display = "none";
        eI(p, i);
    }
    if (window.paint) {
        window.paint.reset(0);
        window.paint.clearAll();
    }
    if (autoplay) {
        return;
    }
    var p = document.getElementById("s0");
    p.style.display = "";
    bE(0);
};
function ExecAnim(ek, fM, trigger) {
    console.log("execanim" + ek + ":" + fM + ":" + trigger);
    if (K.id != "s" + ek || fM == -1) {
        bE(ek);
    } else {
        var tt = K.tl["sp" + trigger];
        if (tt.au && tt.au.length <= fM) return;
        if (tt.au[fM].type == 0) {
            ia();
        }
        tt.dI(tt.au[fM]);
        tt.ak = parseInt(fM) + 1;
        if (noeff == 1) {
            tt.bu();
            return;
        }
        if (tt.bj == -1) {
            tt.bj = 1;
            tt.et();
        }
    }
};
function ExecNext() {
    if (!K.tl || !K.tl["sp-1"] || K.tl["sp-1"].ak == K.tl["sp-1"].au.length) {
        bE(aJ.ak + 1);
    } else {
        K.tl["sp-1"].dG(K.tl["sp-1"]);
    }
};
function StopAllAnimation() {
    lW();
    mI();
    if (K.tl && K.tl["sp-1"] && K.tl["sp-1"].bj != -1) {
        K.tl["sp-1"].bu();
        K.tl["sp-1"].bj = -1;
    }
};
function pk(param) {
    var reg = new RegExp("(^|&)" + param + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]);
    return null;
};
function ExecGoBack() {
    if (K.tl && K.tl["sp-1"] && K.tl["sp-1"].ak != 0) {
        K.tl["sp-1"].gO();
    } else {
        bE(aJ.ak - 1);
    }
};
function taskComplete(currentPage, totalPage) {};
function isNull(value) {
    if (null == value || undefined == value) return true;
    if (typeof value == "string") {
        value = value.replace(/^\s+|\s+$/g, "");
        if ("" == value || "null" == value || "undefined" == value || 0 == value.length) return true;
        else return false;
    }
    if (typeof value == "object") {
        for (var key in value) return false;
        return true;
    }
    return false;
};
function mL(p) {};
function syncPageAnim(slide, anim, trigger, isBack, tm) {
    window.notifyStepChanged && window.notifyStepChanged(slide, anim, trigger, isBack, tm);
};
function syncExec(slide, anim, trigger, isBack) {
    if (isBack) {
        ExecGoBack();
    } else {
        ExecAnim(slide, anim, trigger);
    }
}