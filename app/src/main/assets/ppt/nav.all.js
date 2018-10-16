var currentPageIndex = 0; // 页码
var currentPageStep = -1; // 动画
var baseTimeStamp = 0; // 基准时间
var localRole = 0; // 角色-> 0:老师 1:学生
var TEA = 0;
var STU = 1;
var DELAY_TIME = 500; // 延迟跳转的时间，单位：毫秒
var isLoaded = 0;
var goToPositionTime = 0;
var sendCurrentTimeLineTime = 0;
var lastIndex = -1;
var lastStep = -1;
var isReload = true;
var isStart = false;
////////////////////////////////////// Public /////////////////////////////////////////////////////
/*
 * 智能机、浏览器版本信息:
 *
 */
var browser = {
  versions: function () {
    var u = navigator.userAgent,
      app = navigator.appVersion;
    return { //移动终端浏览器版本信息
      trident: u.indexOf('Trident') > -1, //IE内核
      presto: u.indexOf('Presto') > -1, //opera内核
      webKit: u.indexOf('AppleWebKit') > -1, //苹果、谷歌内核
      gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, //火狐内核
      mobile: !!u.match(/AppleWebKit.*Mobile.*/) || !!u.match(/AppleWebKit/), //是否为移动终端
      ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端
      android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android终端或者uc浏览器
      iPhone: u.indexOf('iPhone') > -1 || u.indexOf('Mac') > -1, //是否为iPhone或者QQHD浏览器
      iPad: u.indexOf('iPad') > -1, //是否iPad
      webApp: u.indexOf('Safari') == -1 //是否web应该程序，没有头部与底部
    };
  }(),
  language: (navigator.browserLanguage || navigator.language).toLowerCase()
};

function fireKeyEvent(el, evtType, keyCode) {
  var doc = el.ownerDocument,
    win = doc.defaultView || doc.parentWindow,
    evtObj;
  if (doc.createEvent) {
    if (win.KeyEvent) {
      evtObj = doc.createEvent('KeyEvents');
      evtObj.initKeyEvent(evtType, true, true, win, false, false, false, false, keyCode, 0);
    }
    else {
      evtObj = doc.createEvent('UIEvents');
      Object.defineProperty(evtObj, 'keyCode', {
        get: function () {
          return this.keyCodeVal;
        }
      });
      Object.defineProperty(evtObj, 'which', {
        get: function () {
          return this.keyCodeVal;
        }
      });
      evtObj.initUIEvent(evtType, true, true, win, 1);
      evtObj.keyCodeVal = keyCode;
      if (evtObj.keyCode !== keyCode) {
        console.log("keyCode " + evtObj.keyCode + " 和 (" + evtObj.which + ") 不匹配");
      }
    }
    el.dispatchEvent(evtObj);
  }
  else if (doc.createEventObject) {
    evtObj = doc.createEventObject();
    evtObj.keyCode = keyCode;
    el.fireEvent('on' + evtType, evtObj);
  }
}

function logger(tagName, logInfo) {
  if (browser.versions.android && window.powerPointUtil) {
    window.powerPointUtil.log(tagName + ": " + logInfo);
  } else {
    console.log(tagName + ": " + logInfo);
  }
}

// 页码加载完成发送消息
function sendLoadedSignal() {
  isLoaded = 1;
  logger("step", "sendLoadedSignal");
  if (browser.versions.android && window.powerPointUtil) {
    if (window.powerPointUtil.onPageLoaded == undefined) {
      logger("Error", "onPageLoaded is undefined");
    } else {
      // delayTime(5000)
      window.powerPointUtil.onPageLoaded();
      logger("signal", "loaded");
    }
  } else {
    logger("signal", "loaded");
  }
}

// 设置角色
function setRole() {
  logger("setRole", window.location.search.split("role=")[1]);
  // logger("window.location", window.location);
  localRole = window.location.search.split("role=")[1];
  isStart = true;
}

// 延迟固定时间

function delayTime(tmp) {
  setTimeout(function () {
    logger("delayed", DELAY_TIME + "ms");
  }, tmp);
}

///////////////////////////////////////////////// Teacher //////////////////////////////////////////////////////////

// 向安卓webview发送数据
function sendCurrentTimeLine(index, step, trigger, isBack, tm) {
  logger("sendCurrentTimeLine", index + ' ' + step);
  var stamp = Date.parse(new Date());
  if (currentPageIndex == 0 && currentPageStep == 0) {
    isLoaded = 1;
    logger("isLoaded", isLoaded);
  }
  if (browser.versions.android && window.powerPointUtil) {
    window.powerPointUtil.sendCurrentTimeLine(parseInt(index), parseInt(step), parseInt(trigger), isBack, parseInt(tm));
  }
}

///////////////////////////////////////////////// Student //////////////////////////////////////////////////////////

// 从第x页跳转第n页第m个动画
function goToPosition(targetPageNumber, actionIndex, trigger, isBack, timeStamp) {
  if (isLoaded == 0) {
    return;
  }
  logger("goToPosition", goToPositionTime++);
  logger("Goto", targetPageNumber + ' ' + actionIndex);
  // 空校验
  if (
    targetPageNumber == undefined ||
    actionIndex == undefined) {
  } else {
    // 整数校验
    targetPageNumber = parseInt(targetPageNumber);
    actionIndex = parseInt(actionIndex);
    if (!(
      integerValidation(parseInt(targetPageNumber)) &&
      integerValidation(parseInt(actionIndex))
    )) {
      logger("跳转参数必须为整数", targetPageNumber + ' ' + actionIndex);
      // 数字合法性校验
    } else if (
      targetPageNumber < 0) {
      logger("跳转参数越界", targetPageNumber + ' ' + actionIndex);
    } else {
      // 开始跳转
      if (actionIndex == -1 && currentPageStep == -1) {
        // -1为翻页
        goToPage(currentPageIndex, targetPageNumber);
        currentPageStep = -1;
      } else if (isBack || currentPageIndex == targetPageNumber && actionIndex < currentPageStep ||
        targetPageNumber < currentPageIndex) {
        //后退操作
        ExecGoBack();
        currentPageStep--;
        if (currentPageStep < -1) {
          if (K.tl["sp-1"] == undefined) {
            currentPageStep = -1;
          } else {
            currentPageStep = K.tl["sp-1"].au.length - 1;
          }
        }
      } else {
        if (isReload) {
          goToPage(currentPageIndex, targetPageNumber);
          goForward(actionIndex);
          isReload = false;
        } else {
          //正常前进
          syncExec(targetPageNumber, actionIndex, trigger, isBack, timeStamp);
        }
        lastIndex = targetPageNumber;
        lastStep = actionIndex;
      }
    }
  }
}

// 跳转第x页
function goToPage(currentPageNumber, targetPageNumber) {
  logger("goToPage", currentPageNumber + ' ' + targetPageNumber);
  if (currentPageNumber == targetPageNumber) {
    return;
  }
  bE(targetPageNumber);
  currentPageIndex = targetPageNumber;
}

// 前进x个动画
function goForward(actionNumber) {
  logger("goForward", actionNumber);
  var targetPageStep = actionNumber;
  if ((parseInt(actionNumber) - parseInt(currentPageStep)) < 0) {
    logger("actionNumber Error", actionNumber + ' ' + targetPageStep + ' ' + ((parseInt(actionNumber) - parseInt(currentPageStep)) < 0) + (parseInt(actionNumber) - parseInt(currentPageStep)));
    return;
  }

  for (var i = currentPageStep + 1; i <= targetPageStep; i++) {
    if (i > K.tl["sp-1"].au.length) {
      break;
    }
    syncExec(currentPageIndex, i, -1, false);
    logger("act", i + ' ' + targetPageStep + ' ' + currentPageStep);
  }

  currentPageStep = actionNumber;
}

// 整数校验
function integerValidation(val) {
  return Number.isInteger(val);
}

function syncPageAnim(slide, anim, trigger, isBack, tm) {
  sendCurrentTimeLine(slide, anim, trigger, isBack, tm);
}

function setFullscreen(isOpen) {
  console.log('setFullscreen: ' + isOpen);
  if (!isOpen) {
    Thum.prototype.hide();
  } else {
    Thum.prototype.show($(this));
  }
}

(function () {
  setRole();
  // if (localRole == STU && browser.versions.android && window.powerPointUtil) {
  //   // 发送加载完毕信号
  //   sendLoadedSignal();
  // }
})();
/*Tools.js*/
;(function () {
  var tools = {
    isPc: function () {
      var result = true;
      var ua = navigator.userAgent.toLowerCase();
      if (ua.indexOf("android") >= 0 || ua.indexOf("iphone") >= 0 || ua.indexOf("ipad") >= 0) {
        result = false;
      }
      return result;
    },
    stopPropagation: function (e) {
      e = e || window.event;
      if (e.stopPropagation)
        e.stopPropagation();
      else {
        e.cancelBubble = true;
      }
    },
    preventDefault: function (e) {
      e = e || window.event;
      if (e.preventDefault) {
        e.preventDefault()
      } else {
        e.returnValue = false;
      }
    }
  }
  if (typeof window['Tools'] === 'undefined') {
    window['Tools'] = tools;
  }
})()
/*nav.js*/
var thum1;
var prevNum = 0;

//跳转函数
function bE(bP) {
  if(isStart){
    isStart = false;
    if (localRole == STU ) {
      // 发送加载完毕信号
      sendLoadedSignal();
    }
  }
  if (prevNum === bP || bP >= _note.length) return;

  prevNum = bP
  console.log("gopage " + bP);
  resetPage(bP)
  var pageNum = bP + 1;
  if (pageNum > _note.length) {
    pageNum = _note.length
  }
  $(".th_page span").html(pageNum + "/" + _note.length);//页码
  $(".swiper-slide").eq(bP).addClass('active').siblings().removeClass('active');
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
    } else if (autoplay) {
    } else {
      alert("您已到达最后一页，点击鼠标将从头开始播放");
    }
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

//页面初始化翻转函数
function fj() {
  console.log('页面开始改变大小')
  console.log(thum1)
  var zoom;
  var root = document.getElementById("root");
  var thpage = $('.th_page')
  var control = $('.control')
  var thumbnailbox = $('.thumbnail_box')
  if (mW > 0 && !iscef) {
    aW = document.documentElement.clientHeight || document.body.clientHeight;
    bV = document.documentElement.clientWidth || document.body.clientWidth;
    var clientWidth = bV;
    var clientHeight = aW;
    if ((aW - bV) * (aP - aS) < 0) {
      // alert('水平方向')
      var bG = aW;
      aW = bV;
      bV = bG;
      $(root).css({
        left: aW + 'px',
        '-webkit-transform': 'rotate(90deg)',
        '-moz-transform': 'rotate(90deg)',
        '-ms-transform': 'rotate(90deg)',
        '-o-transform': 'rotate(90deg)',
        'transform': 'rotate(90deg)',
        '-webkit-transform-origin': '0 0',
        '-moz-transform-origin': '0 0',
        '-ms-transform-origin': '0 0',
        '-o-transform-origin': '0 0',
        'transform-origin': '0 0'
      })
      thpage.css({
        top: '190px',
        left: '10%',
        '-webkit-transform': 'rotate(90deg)',
        '-moz-transform': 'rotate(90deg)',
        '-ms-transform': 'rotate(90deg)',
        '-o-transform': 'rotate(90deg)',
        'transform': 'rotate(90deg)',
        '-webkit-transform-origin': '0 0',
        '-moz-transform-origin': '0 0',
        '-ms-transform-origin': '0 0',
        '-o-transform-origin': '0 0',
        'transform-origin': '0 0',
      })
      control.css({
        'left': '5%',
        'right': 'auto',
      })
      thumbnailbox.css({
        '-webkit-transform': 'rotate(90deg) translateY(-' + clientWidth + 'px)',
        '-moz-transform': 'rotate(90deg) translateY(-' + clientWidth + 'px)',
        '-ms-transform': 'rotate(90deg) translateY(-' + clientWidth + 'px)',
        '-o-transform': 'rotate(90deg) translateY(-' + clientWidth + 'px)',
        'transform': 'rotate(90deg) translateY(-' + clientWidth + 'px)',
        '-webkit-transform-origin': '0 0',
        '-moz-transform-origin': '0 0',
        '-ms-transform-origin': '0 0',
        '-o-transform-origin': '0 0',
        'transform-origin': '0 0',
        height: clientWidth + 'px',
        marginTop: 0,
        marginLeft: 'auto',
        left: 0,
      })
      var fullWidth = $(window).height();
      var fullHeight = $(window).width();
      console.log(fullWidth, fullHeight)//1280*800
      var mainWidth = fullWidth - 174;
      var rootWidth = fullWidth;
      var rootHeight = fullHeight;
      console.log(rootWidth, rootHeight)
      var currentRootWidth = rootWidth * mainWidth / fullWidth;
      var diff = fullHeight - rootHeight * mainWidth / fullWidth
      console.log(fullWidth, mainWidth, rootWidth, currentRootWidth)
      $('#root').css({
        'top': '174px',
        '-webkit-transform': 'rotate(90deg) scale(' + mainWidth / fullWidth + ') translateY(' + diff / 2 + 'px)',
        '-moz-transform': 'rotate(90deg) scale(' + mainWidth / fullWidth + ') translateY(' + diff / 2 + 'px)',
        '-ms-transform': 'rotate(90deg) scale(' + mainWidth / fullWidth + ') translateY(' + diff / 2 + 'px)',
        '-o-transform': 'rotate(90deg) scale(' + mainWidth / fullWidth + ') translateY(' + diff / 2 + 'px)',
        'transform': 'rotate(90deg) scale(' + mainWidth / fullWidth + ') translateY(' + diff / 2 + 'px)',
      })
      eY = 1;
    } else {
      // alert('垂直方向')
      root.style[hx] = "";
      thpage.css({
        right: 'auto',
        bottom: '10%',
        left: '190px',
        top: 'auto',
        '-webkit-transform': 'rotate(0deg)',
        '-moz-transform': 'rotate(0deg)',
        '-ms-transform': 'rotate(0deg)',
        '-o-transform': 'rotate(0deg)',
        transform: 'rotate(0deg)',
      })
      control.css({
        'left': 'auto',
        'right': '5%',
      })
      thumbnailbox.css({
        '-webkit-transform': 'none',
        '-moz-transform': 'none',
        '-ms-transform': 'none',
        '-o-transform': 'none',
        'transform': 'none',
        '-webkit-transform-origin': 'center',
        '-moz-transform-origin': 'center',
        '-ms-transform-origin': 'center',
        '-o-transform-origin': 'center',
        'transform-origin': 'center',
        height: clientHeight + 'px',
        left: 0,
        marginLeft: 'auto',
        marginTop: 'auto'
      })
      var fullWidth = $('body').width();
      var mainWidth = fullWidth - 174;
      var rootWidth = $('#root').width();
      var currentRootWidth = rootWidth * mainWidth / fullWidth;
      console.log(fullWidth, mainWidth, rootWidth, currentRootWidth)
      $('#root').css({
        left: '87px',
        top: 0,
        '-webkit-transform': 'scale(' + (mainWidth / fullWidth) + ')',
        '-moz-transform': 'scale(' + (mainWidth / fullWidth) + ')',
        '-ms-transform': 'scale(' + (mainWidth / fullWidth) + ')',
        '-o-transform': 'scale(' + (mainWidth / fullWidth) + ')',
        'transform': 'scale(' + (mainWidth / fullWidth) + ')',
        '-webkit-transform-origin': 'center center',
        '-moz-transform-origin': 'center center',
        '-ms-transform-origin': 'center center',
        '-o-transform-origin': 'center center',
        'transform-origin': 'center center',
      })
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

/*Thum.js*/
;(function ($) {
  var opened = true;//thum opened
  function Thum(options) {
    var defaultOption = {
      direction: 'vertical',
      width: 134,
      height: 120,
      current: 0,//默认选择第一个
    }
    this.mergedOptions = $.extend(defaultOption, options)
    this.init()
  }

  Thum.prototype = {
    constructor: Thum,
    init: function () {
      this.initDom()
      this.initEvent();
    },
    initDom: function () {
      //初始化列表
      var str = '<div class="thumbnail"><div class="thumbnail_box"><div class="swiper-container "><ul class="swiper-wrapper">';
      for (var i = 0; i < _note.length; i++) {
        var mypage = i + 1;
        str += '<li class="swiper-slide"><div class="show_box">' + $("#main").find("#s" + i).html() + '</div><span class="page_box"  data-page="' + i + '">第' + mypage + '页</span></li>'
      }
      str += '</ul></div></div></div>';
      $("body").append(str);
      //初始化页码及控件
      $('body').append('<div class="th_page"><span></span></div><div class="control"><span></span></div>')
      $(".th_page span").html(1 + "/" + _note.length);//页码
      $(".swiper-slide").eq(0).addClass('active').siblings().removeClass('active');
    },
    initEvent: function () {
      var self = this;
      self.goPage = window.bE;
      $('.control')
        .on('mouseover', function (e) {
          console.log('over')
          e.stopPropagation()
          window.bE = self.noop;
        })
        .on('mouseout', function (e) {
          console.log('out')
          e.stopPropagation()
          window.bE = self.goPage
        })
        .on('touchstart', function (e) {
          e.stopPropagation()
          console.log("send fs signal: " + !opened);
          if (browser.versions.android && window.powerPointUtil) {
            console.log("send fs signal: " + !opened);
            window.powerPointUtil.sendFullScreen(!opened);
          }
          if (opened) {
            self.hide()
          } else {
            self.show($(this))
          }
        })
      $(".swiper-slide").on("click", function (e) {
        e.stopPropagation()
        self.select($(this));
      });
      self.drag($('.swiper-wrapper')[0]);
    },
    drag: function (obj) {
      var self = this;
      var isDown = false;
      var oldPos, curPos, offset, diff, swiperWrapperTop, swiperWrapperHeight, swiperContainerHeight;
      var isPortrait = window.orientation === 0
      $(window).on("orientationchange", function () {
        isPortrait = window.orientation === 0
      });

      function down(event) {
        Tools.stopPropagation(event)
        /*don't drag if less than the width of a screen*/
        var screenWidth = isPortrait ? document.documentElement.clientWidth : document.documentElement.clientHeight;
        swiperWrapperHeight = $('.swiper-wrapper').height();
        swiperContainerHeight = $('.swiper-container').height();
        if (screenWidth > swiperWrapperHeight) {
          return;
        }
        isDown = true;
        var touch = event.touches ? event.touches[0] : event;
        oldPos = isPortrait ? touch.clientX : touch.clientY;
        offset = isPortrait ? obj.offsetTop : obj.offsetTop;
      }


      function move(event) {
        Tools.stopPropagation(event)
        if (isDown) {
          var touch = event.touches ? event.touches[0] : event;
          diff = isPortrait ? touch.clientX - oldPos : touch.clientY - oldPos;
          curPos = isPortrait ? touch.clientX : touch.clientY;
          var tempTop;
          if (isPortrait) {
            tempTop = offset - diff;
          } else {
            tempTop = offset + diff;
          }
          //修正
          if (tempTop > 0) {
            tempTop = 0;
          }
          if (tempTop < swiperContainerHeight - swiperWrapperHeight) {
            tempTop = swiperContainerHeight - swiperWrapperHeight
          }
          obj.style.top = tempTop + 'px'
          //阻止页面的滑动默认事件
          document.addEventListener("touchmove", function (event) {
            Tools.preventDefault(event)
          }, {passive: false});
        }
      }

      function end(event) {
        isDown = false;
        document.removeEventListener('mousemove', move)
        document.removeEventListener('mouseup', end)
      }

      obj.addEventListener("mousedown", down, false);
      obj.addEventListener("touchstart", down, false)

      document.addEventListener("mousemove", move, false);
      obj.addEventListener("touchmove", move, false)

      document.body.addEventListener("mouseup", end, false);
      obj.addEventListener("touchend", end, false);
    },
    show: function (ele, bool) {
      var self = this;
      opened = true;
      aW = document.documentElement.clientHeight;
      bV = document.documentElement.clientWidth;
      if ((aW - bV) * (aP - aS) < 0) {
        var fullWidth = $(window).height();
        var mainWidth = fullWidth - 174;
        var rootWidth = $('#root').width();
        var rootHeight = $('#root').height();
        var currentRootWidth = rootWidth * mainWidth / fullWidth;
        var windowWidth = $(window).width()
        var diff = windowWidth - rootHeight * mainWidth / fullWidth
        $('#root').css({
          'top': '174px',
          '-webkit-transform': 'rotate(90deg) scale(' + mainWidth / fullWidth + ') translateY(' + diff / 2 + 'px)',
          '-moz-transform': 'rotate(90deg) scale(' + mainWidth / fullWidth + ') translateY(' + diff / 2 + 'px)',
          '-ms-transform': 'rotate(90deg) scale(' + mainWidth / fullWidth + ') translateY(' + diff / 2 + 'px)',
          '-o-transform': 'rotate(90deg) scale(' + mainWidth / fullWidth + ') translateY(' + diff / 2 + 'px)',
          'transform': 'rotate(90deg) scale(' + mainWidth / fullWidth + ') translateY(' + diff / 2 + 'px)',
        })
        $('.th_page').css({
          top: '190px'
        })
        $('.thumbnail_box').css({
          marginTop: 0,
          marginLeft: 'auto'
        })
      } else {
        var fullWidth = $('body').width();
        var mainWidth = fullWidth - 174;
        var rootWidth = $('#root').width();
        var currentRootWidth = rootWidth * mainWidth / fullWidth;
        $('#root').css({
          left: '87px',
          '-webkit-transform': 'scale(' + (mainWidth / fullWidth) + ')',
          '-moz-transform': 'scale(' + (mainWidth / fullWidth) + ')',
          '-ms-transform': 'scale(' + (mainWidth / fullWidth) + ')',
          '-o-transform': 'scale(' + (mainWidth / fullWidth) + ')',
          transform: 'scale(' + (mainWidth / fullWidth) + ')',
          '-webkit-transform-origin': Math.floor((mainWidth - currentRootWidth) / 2) + ' center',
          '-moz-transform-origin': Math.floor((mainWidth - currentRootWidth) / 2) + ' center',
          '-ms-transform-origin': Math.floor((mainWidth - currentRootWidth) / 2) + ' center',
          '-o-transform-origin': Math.floor((mainWidth - currentRootWidth) / 2) + ' center',
          'transform-origin': Math.floor((mainWidth - currentRootWidth) / 2) + ' center'
        })
        $('.th_page').css({
          left: '190px'
        })
        $('.thumbnail_box').css({
          marginLeft: '0px',
          marginTop: 'auto'
        })
      }
      ele.hide();
      $('.control').css({
        display: 'block'
      })
      $(".swiper-slide").eq(parseInt(ele.find('.page_box').attr("data-page"))).addClass('active').siblings().removeClass('active');
      $('.control span').toggleClass('hide')
    },
    hide: function () {
      if (opened) {
        aW = document.documentElement.clientHeight;
        bV = document.documentElement.clientWidth;
        if ((aW - bV) * (aP - aS) < 0) {
          $('#root').css({
            top: 0,
            '-webkit-transform': 'rotate(90deg)',
            '-moz-transform': 'rotate(90deg)',
            '-ms-transform': 'rotate(90deg)',
            '-o-transform': 'rotate(90deg)',
            'transform': 'rotate(90deg)',
          })
          $('.th_page').css({
            top: '2%',
            '-webkit-transition': 'left .1s',
            '-moz-transition': 'left .1s',
            '-ms-transition': 'left .1s',
            '-o-transition': 'left .1s',
            'transition': 'left .1s',
          })
          $(".thumbnail_box").css({
            marginTop: '-100%'
          })
        } else {
          $('#root').css({
            left: 0,
            '-webkit-transform': 'scale(1)',
            '-moz-transform': 'scale(1)',
            '-ms-transform': 'scale(1)',
            '-o-transform': 'scale(1)',
            'transform': 'scale(1)',
            '-webkit-transform-origin': 'center center',
            '-moz-transform-origin': 'center center',
            '-ms-transform-origin': 'center center',
            '-o-transform-origin': 'center center',
            'transform-origin': 'center center'
          })
          $('.th_page').css({
            left: '2%',
            '-webkit-transition': 'left .1s',
            '-moz-transition': 'left .1s',
            '-ms-transition': 'left .1s',
            '-o-transition': 'left .1s',
            'transition': 'left .1s',
          })
          $('.thumbnail .thumbnail_box').css({
            marginLeft: '-100%'
          })
        }
        opened = false;
      }
      var pg;
      if (window.aJ) pg = aJ.ak;
      else pg = cs.id.substr(1);
      $('.control span').toggleClass('hide')
    },
    select: function (ele) {
      opened = true;
      ele.addClass('active').siblings().removeClass('active');
      bE(parseInt(ele.find('.page_box').attr("data-page")));
      var pg;
      if (window.aJ) pg = aJ.ak;
      else pg = cs.id.substr(1);
    },
    noop: function () {
      console.log('用来暂停页面跳转功能的')
    },
  }
  if (typeof window['Thum'] === "undefined") {
    window['Thum'] = Thum;
  }
})(jQuery)

$(function () {
  pptcolor = '#2B2B2B'
  $('meta[name=viewport]').attr('content', 'width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no')
  $('head').append('<meta name="screen-orientation" content="portrait">')
  $('head').append('<meta name="full-screen" content="yes">')
  $('head').append('<meta name="browsermode" content="application">')
  $('head').append('<meta name="x5-orientation" content="portrait">')
  $('head').append('<meta name="x5-fullscreen" content="true">')
  $('head').append('<meta name="x5-page-mode" content="app">')
  $('head').append('<meta name="HandheldFriendly" content="true">')
  $('head').append('<meta name="MobileOptimized" content="320">')
  //防止移动浏览器自带下拉刷新功能
  $('body').css({
    overflow: 'hidden',
  })
  thum1 = new Thum()
})