# MayaCharm

## [Unreleased]

## [3.2.7] - 2023-09-22
### Fixes
- now properly recognized UTf-8 code points in remotely executed scripts

## [3.2.6] - 2022-02-31
### Added
- PyCharm 2021.3 support
 
## [3.2.5] - 2021-08-03
### Added
- PyCharm 2021.2 support

## [3.2.4] - 2021-04-8
### Added
- support Python 3

## [3.2.3] - 2021-04-7
### Added
- PyCharm 2021 support

## [3.2.2] - 2021-03-17
### Added
- Display of Maya path in the "Attach to process popup"
### Fixes
- Issue where the proper mayaSdk port number was not always selected when attaching to a Maya instance

## [3.2.1] - 2021-03-13
### Added
- Support for PyCharm 2020.3
### Modified
- Updated gradle and build system

## [3.1.2] - 2020-04-05
### Added
- Improved handling and detection of MayaPy version when adding new interpreters

### Fixed
- Fixed issues where MayaCharm could not detect Maya instances that were launched via script and not executed directly

## [3.1.1] - 2019-09-30
### Added
- Now forces utf8 encoding when executing the selection

### Modified
- All strings have be moved to a resource bundle for localization purposes

### Fixed
- Settings panel properly updates with Maya sdks now when interpreter settings are changed
- Settings panel now has proper add and remove buttons for Maya SDKs that will properly add or remove the interpreter as well setup the command port info for the sdk

### Removed
- Removed debug run config, since it was unreliable due to a race condition when it would execute your maya code

## 3.0.2 - 2019-03-31
### Fixed
- Fixed issue with execute commands being greyed out in Pycharm 2019.1

## 3.0.1 - 2019-01-13
### Added
- Attach to Process now shows maya instances you can attach to

### Fixed
- Fixed bug that prevented MayaCharm from finding Maya Instances when launched with arguments

## 3.0.0 - 2018-11-27
### Added
- Ported to Kotlin
- Better support for multiple Maya installs
- Removed dependencies on PyCharm Professional's remote debugger as well as PyCharm Professional

[3.2.2]: https://github.com/cmcpasserby/MayaCharm/releases/tag/v3.2.2
[3.2.1]: https://github.com/cmcpasserby/MayaCharm/releases/tag/v3.2.1
[3.1.2]: https://github.com/cmcpasserby/MayaCharm/releases/tag/v3.1.2
[3.1.1]: https://github.com/cmcpasserby/MayaCharm/releases/tag/v3.1.1
[3.0.0]: https://github.com/cmcpasserby/MayaCharm/releases/tag/v3.0.0
