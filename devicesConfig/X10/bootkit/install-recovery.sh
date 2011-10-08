#!/system/xbin/sh

# Check if kernel is sysinit capable
export INITD1=`grep -F init.d /init* 2>/dev/null`
export INITD2=`grep -F init.d /etc/init* 2>/dev/null`
export INITD3=`grep -F sysinit /init* 2>/dev/null`
export INITD4=`grep -F sysinit /etc/init* 2>/dev/null`

# If not, run it from here
if test -z "$INITD1" -a -z "$INITD2" -a -z "$INITD3" -a -z "$INITD4"
then
	exec /system/bin/sysinit
fi