var ScriptUtils = com.lumens.processor.script.ScriptUtils;
var SimpleDateFormat = java.text.SimpleDateFormat;
var Calendar = java.util.Calendar;

function getElementValue(ctx, path) {
  return ScriptUtils.getElement(ctx, path);
}

function now() {
  var cal = Calendar.getInstance();
  return cal.getTime();
}


function dateFormat(date, format) {
  var dateFormat = new SimpleDateFormat(format);
  return dateFormat.format(date);
}