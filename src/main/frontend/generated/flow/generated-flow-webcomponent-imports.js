import { injectGlobalWebcomponentCss } from 'Frontend/generated/jar-resources/theme-util.js';

import { injectGlobalCss } from 'Frontend/generated/jar-resources/theme-util.js';

import { css, unsafeCSS, registerStyles } from '@vaadin/vaadin-themable-mixin';
import $cssFromFile_0 from '@vaadin/vaadin-lumo-styles/lumo.css?inline';
import '@vaadin/vertical-layout/src/vaadin-vertical-layout.js';
import '@vaadin/text-field/src/vaadin-text-field.js';
import '@vaadin/tooltip/src/vaadin-tooltip.js';
import '@vaadin/password-field/src/vaadin-password-field.js';
import '@vaadin/button/src/vaadin-button.js';
import 'Frontend/generated/jar-resources/disableOnClickFunctions.js';
import '@vaadin/icons/vaadin-iconset.js';
import '@vaadin/icon/src/vaadin-icon.js';
import '@vaadin/app-layout/src/vaadin-app-layout.js';
import '@vaadin/horizontal-layout/src/vaadin-horizontal-layout.js';
import '@vaadin/app-layout/src/vaadin-drawer-toggle.js';
import 'Frontend/generated/jar-resources/menubarConnector.js';
import 'Frontend/generated/jar-resources/contextMenuConnector.js';
import '@vaadin/menu-bar/src/vaadin-menu-bar.js';
import '@vaadin/context-menu/src/vaadin-context-menu.js';
import 'Frontend/generated/jar-resources/flow-component-renderer.js';
import 'Frontend/generated/jar-resources/flow-component-directive.js';
import 'lit';
import 'Frontend/generated/jar-resources/contextMenuTargetConnector.js';
import '@vaadin/component-base/src/gestures.js';
import '@vaadin/grid/src/vaadin-grid.js';
import '@vaadin/grid/src/vaadin-grid-column.js';
import '@vaadin/grid/src/vaadin-grid-sorter.js';
import '@vaadin/checkbox/src/vaadin-checkbox.js';
import 'Frontend/generated/jar-resources/gridConnector.ts';
import '@vaadin/component-base/src/debounce.js';
import '@vaadin/component-base/src/async.js';
import '@vaadin/grid/src/vaadin-grid-active-item-mixin.js';
import 'Frontend/generated/jar-resources/vaadin-grid-flow-selection-column.js';
import '@vaadin/grid/src/vaadin-grid-column-group.js';
import 'Frontend/generated/jar-resources/lit-renderer.ts';
import 'lit/directives/live.js';
import '@vaadin/notification/src/vaadin-notification.js';
import '@vaadin/dialog/src/vaadin-dialog.js';
import '@vaadin/details/src/vaadin-details.js';
import '@vaadin/combo-box/src/vaadin-combo-box.js';
import 'Frontend/generated/jar-resources/comboBoxConnector.js';
import '@vaadin/combo-box/src/vaadin-combo-box-placeholder.js';
import '@vaadin/multi-select-combo-box/src/vaadin-multi-select-combo-box.js';
import '@vaadin/checkbox-group/src/vaadin-checkbox-group.js';
import 'Frontend/generated/jar-resources/vaadin-big-decimal-field.js';
import '@vaadin/component-base/src/define.js';
import '@vaadin/integer-field/src/vaadin-integer-field.js';
import '@vaadin/date-picker/src/vaadin-date-picker.js';
import 'Frontend/generated/jar-resources/datepickerConnector.js';
import 'date-fns/parse';
import '@vaadin/date-picker/src/vaadin-date-picker-helper.js';
import '@vaadin/tabsheet/src/vaadin-tabsheet.js';
import '@vaadin/tabs/src/vaadin-tabs.js';
import '@vaadin/tabs/src/vaadin-tab.js';
import '@vaadin/form-layout/src/vaadin-form-layout.js';
import '@vaadin/form-layout/src/vaadin-form-item.js';
import '@vaadin/form-layout/src/vaadin-form-row.js';
import '@vaadin/text-area/src/vaadin-text-area.js';
import '@vaadin/select/src/vaadin-select.js';
import 'Frontend/generated/jar-resources/selectConnector.js';
import '@vaadin/email-field/src/vaadin-email-field.js';
import '@vaadin/number-field/src/vaadin-number-field.js';
import '@vaadin/common-frontend/ConnectionIndicator.js';
import 'Frontend/generated/jar-resources/ReactRouterOutletElement.tsx';
import 'react-router';
import 'react';

injectGlobalWebcomponentCss($cssFromFile_0.toString());
const loadOnDemand = (key) => {
  const pending = [];
  if (key === '55f4b325a56d94ef003ca8c6fa6210c60474e534469cf97e6dc379edd33ae114') {
    pending.push(import('./chunks/chunk-827012bdbabf496f7299a89fd519c66c07ae83b0df3d18f082bafe1a39a6b1a5.js'));
  }
  if (key === '429c970acd9a171da689269689af1b45b3617213f4ae270a819615bf15fb662a') {
    pending.push(import('./chunks/chunk-827012bdbabf496f7299a89fd519c66c07ae83b0df3d18f082bafe1a39a6b1a5.js'));
  }
  if (key === '5c9a9b8119589fc6f31e03b628fee7779be2f95de5cb2354b728f6731031627a') {
    pending.push(import('./chunks/chunk-827012bdbabf496f7299a89fd519c66c07ae83b0df3d18f082bafe1a39a6b1a5.js'));
  }
  if (key === 'f78a78f76724f971fc30050bb102dbcbc2e9c8fcd9e42071c246a17e323cac65') {
    pending.push(import('./chunks/chunk-827012bdbabf496f7299a89fd519c66c07ae83b0df3d18f082bafe1a39a6b1a5.js'));
  }
  if (key === '8722081c6f99a681f2bc499885a8781e174a3b724c7afa215a507ec89b21cff0') {
    pending.push(import('./chunks/chunk-827012bdbabf496f7299a89fd519c66c07ae83b0df3d18f082bafe1a39a6b1a5.js'));
  }
  if (key === 'f1285d8c281dc6ef5259bfdac8a9eac044fa62eff67c8b38ce501c91ba5e393c') {
    pending.push(import('./chunks/chunk-9a8becacbf468fc868a435a3d91dbcef8628727171617daca2ddb299288fb765.js'));
  }
  if (key === 'e7fae06c3dfe273ec338596fe26a50df0aaf1a8274b1a1d13bc9f7a674ddc458') {
    pending.push(import('./chunks/chunk-9a8becacbf468fc868a435a3d91dbcef8628727171617daca2ddb299288fb765.js'));
  }
  if (key === '11ad0bf9ba32e0975d12575acc90ffa5166e00ea251722798ed2e0cab4d5b9b0') {
    pending.push(import('./chunks/chunk-827012bdbabf496f7299a89fd519c66c07ae83b0df3d18f082bafe1a39a6b1a5.js'));
  }
  if (key === 'b9651b14b31d0da7262627dfd4035bf089e3c9bfe4ca351bc6c7ca80024ab9a6') {
    pending.push(import('./chunks/chunk-827012bdbabf496f7299a89fd519c66c07ae83b0df3d18f082bafe1a39a6b1a5.js'));
  }
  if (key === 'b374fbd55cf42c21a1e5fa557e99101828d357fac74d00c7346c2f485b226111') {
    pending.push(import('./chunks/chunk-9a8becacbf468fc868a435a3d91dbcef8628727171617daca2ddb299288fb765.js'));
  }
  if (key === '1eb9fff8686697cd6e8ff8b4bbdc1377e11208d17927c3f7bda5b7ca2489b08e') {
    pending.push(import('./chunks/chunk-eb6b42d169f8fd1074564f03673cfb1abf026252b2cd2996ac67a7b446150c52.js'));
  }
  if (key === '5f972dc38cd9834a72c681a680fb83b5a68715bbb77c921003ac4803b7a5e23a') {
    pending.push(import('./chunks/chunk-2e8569b2bcd53e55e3373b2698c05d1f64b38c22453324c2f46c6fa3ec34be29.js'));
  }
  if (key === '1cec3bc79498004bc39e0ce0efcd93028eee0dcc6d71d57ecc988e61ea4c820c') {
    pending.push(import('./chunks/chunk-9a8becacbf468fc868a435a3d91dbcef8628727171617daca2ddb299288fb765.js'));
  }
  if (key === '10559d76b01e07723e3e709645d698af245838e69767fb2e486dd42a2cc49d3c') {
    pending.push(import('./chunks/chunk-827012bdbabf496f7299a89fd519c66c07ae83b0df3d18f082bafe1a39a6b1a5.js'));
  }
  if (key === 'c29f8bb486c0c40e4bb6b101903a85323b842777c3518bf7079bf30e3065dbe3') {
    pending.push(import('./chunks/chunk-c82b0fe7c1d4c8fbba19c01c638426564135fc4936291ac02b3c80ceeb6a87e4.js'));
  }
  if (key === '3fe517f56749a1b14c17261b71fe1a40ebff8f72b3d098bbb2c29e0bfa0577b8') {
    pending.push(import('./chunks/chunk-827012bdbabf496f7299a89fd519c66c07ae83b0df3d18f082bafe1a39a6b1a5.js'));
  }
  if (key === 'ca213bf92faa8eb4295b112cda4244286516a87b7071bf287a6ebb41d32ce448') {
    pending.push(import('./chunks/chunk-9a8becacbf468fc868a435a3d91dbcef8628727171617daca2ddb299288fb765.js'));
  }
  if (key === 'ba8f50c29cb8b830f21220d00b6f0bce085624e580160300bb7c913980fd4006') {
    pending.push(import('./chunks/chunk-3e53f418bb581034f8803fa3aaeabe70e16cff4bf1694dd53a19929705d22f7d.js'));
  }
  if (key === '25ff84f9f48910950ff99240acf16717dc0d3abd203e5cd043f5148161d149d6') {
    pending.push(import('./chunks/chunk-827012bdbabf496f7299a89fd519c66c07ae83b0df3d18f082bafe1a39a6b1a5.js'));
  }
  if (key === 'f227d13af71665abbea3b2ccd0aceb0063cd567214e8c0611a63040b355c87a3') {
    pending.push(import('./chunks/chunk-e98b49cbb691b9808cc2c7338cb6f5a2ba4aca9ad79e48a6fe7d636260bb6c69.js'));
  }
  if (key === '75e98cb59ecc61db404d16ae483c88217c26f39b166bd443ded71e9db8dff8a4') {
    pending.push(import('./chunks/chunk-9a8becacbf468fc868a435a3d91dbcef8628727171617daca2ddb299288fb765.js'));
  }
  if (key === '0b065c58eff820419884816b5d967ad0336806b8b1c3bcaeda6632b061ef5dd6') {
    pending.push(import('./chunks/chunk-827012bdbabf496f7299a89fd519c66c07ae83b0df3d18f082bafe1a39a6b1a5.js'));
  }
  if (key === '7233ad495e3b8c614c6d13344d0258873623b49abeb0fe92f43807844ffdde21') {
    pending.push(import('./chunks/chunk-db406a535b3611d9b2e2a336cee26576a43bb14978c3ae14d75cf3000221c38a.js'));
  }
  if (key === 'eaaa3c90336c7e8297e03c9c796feb89b43cb47a5854760f7374abd366ce381f') {
    pending.push(import('./chunks/chunk-7df48eae3185c3212d169067c1e45e4d4cbc51d3a83d0534fcc357a41dd584a9.js'));
  }
  if (key === '104afb317c49dba74da2193a424c187de3a004f0c97ca710428b9165c624e343') {
    pending.push(import('./chunks/chunk-827012bdbabf496f7299a89fd519c66c07ae83b0df3d18f082bafe1a39a6b1a5.js'));
  }
  if (key === '9e85a49c175b06ab2aecbab1e9c97801a54d31370b9730a49cc9e576ebf4ab6a') {
    pending.push(import('./chunks/chunk-eb6b42d169f8fd1074564f03673cfb1abf026252b2cd2996ac67a7b446150c52.js'));
  }
  if (key === '6f20a1d0126e3cb6f1ad33532e68008d79f2b8bc8ec6259e6e8bf3af5cb838cb') {
    pending.push(import('./chunks/chunk-e98b49cbb691b9808cc2c7338cb6f5a2ba4aca9ad79e48a6fe7d636260bb6c69.js'));
  }
  if (key === '70f2b3bbda25c6d050672429aeb8ca7e6f6a518587c12cc9c9ee17206f8f539a') {
    pending.push(import('./chunks/chunk-b89ad90d362b740b8f8361f74032fee9374925dc46af3ba0d0788c487ff4ec79.js'));
  }
  if (key === '78d74da9f273006eb11196d0a8cff65f043a0d3485da7ace6b4c1b1eeebdfc1a') {
    pending.push(import('./chunks/chunk-827012bdbabf496f7299a89fd519c66c07ae83b0df3d18f082bafe1a39a6b1a5.js'));
  }
  return Promise.all(pending);
}
window.Vaadin = window.Vaadin || {};
window.Vaadin.Flow = window.Vaadin.Flow || {};
window.Vaadin.Flow.loadOnDemand = loadOnDemand;
window.Vaadin.Flow.resetFocus = () => {
 let ae=document.activeElement;
 while(ae&&ae.shadowRoot) ae = ae.shadowRoot.activeElement;
 return !ae || ae.blur() || ae.focus() || true;
}