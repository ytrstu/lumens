var ProcessorUtils = com.lumens.processor.ProcessorUtils;

function getElementValue(ctx, path) {
  var elem = ProcessorUtils.getElement(ctx, path);
  if(elem.isShort())
    return elem.getShort();
  else if(elem.isInt())
    return elem.getInt();
  else if(elem.isLong())
    return elem.getLong();
  else if(elem.isFloat())
    return elem.getFloat();
  else if(elem.isDouble())
    return elem.getDouble();
  else if(elem.isDate())
    return elem.getDate();
  else if(elem.isBinary())
    return elem.getBinary();
  else if(elem.isString())
    return elem.getString();

  return elem.getValue();
}
