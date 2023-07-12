DESCRIPTION = "SystemInfo Recipe"
SECTION = "console/utils"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=391683d5b54c081038e3134e682fb7e3"

SRC_URI = "https://github.com/YoginiMulay/sysinfo.git"
# SRCREV = "93572cd813a5fcdfe5a7e798f057c79bece84bb0"

S = "${WORKDIR}/sysinfo"

CXXFLAGS +=" -DYOCTO_BUILD"
#filesystem library available from C++17 only
CXXFLAGS_append_dunfell += " -std=c++17"

inherit pkgconfig syslog-ng-config-gen
SYSLOG-NG_FILTER = "sysinfo"
SYSLOG-NG_SERVICE_sysinfo = "sysinfo.service"
SYSLOG-NG_DESTINATION_sysinfo = "sysinfo.log"
SYSLOG-NG_LOGRATE_sysinfo = "high"

CXXFLAGS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' `pkg-config --cflags libsafec`', ' -fPIC', d)}"
CXXFLAGS_append_client = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' `pkg-config --cflags libsafec`', ' -fPIC', d)}"

LDFLAGS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' `pkg-config --libs libsafec`', '', d)}"
CXXFLAGS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', '', ' -DSAFEC_DUMMY_API', d)}"
CXXFLAGS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' -DSAFEC_RDKV', '', d)}"

CXXFLAGS += "-I${STAGING_INCDIR}/rdk/iarmbus -DENABLE_SD_NOTIFY -I${STAGING_INCDIR}/rdk/iarmmgrs-hal"
CFLAGS += "-I${STAGING_INCDIR}/rdk/iarmbus -DENABLE_SD_NOTIFY -I${STAGING_INCDIR}/rdk/iarmmgrs-hal"
LDFLAGS += "-lsystemd -lsqlite3 -lsecure_wrapper -lcurl"

DEPENDS = "glib-2.0 iarmbus iarmmgrs dbus cjson libnl sqlite3 libsyswrapper rdk-logger xupnp curl"

inherit autotools systemd

do_install_append() {
        install -d ${D}${bindir}
        install -d ${D}${sysconfdir}
        install -d ${D}${systemd_unitdir}/system

        install -m 0644 ${S}/sysinfo.service ${D}${systemd_unitdir}/system
        
        # install -d ${D}${includedir}
        # install -m 0644 ${S}/systinfo.h ${D}${includedir}
}

SYSTEMD_SERVICE_${PN} += "sysinfo.service"
FILES_${PN} += "${bindir}/*"
FILES_${PN} += "${systemd_unitdir}/system/sysinfo.service"

PARALLEL_MAKE = ""

