import '@vaadin/polymer-legacy-adapter/style-modules.js';
import '@vaadin/vertical-layout/theme/lumo/vaadin-vertical-layout.js';
import '@vaadin/text-field/theme/lumo/vaadin-text-field.js';
import '@vaadin/tooltip/theme/lumo/vaadin-tooltip.js';
import '@vaadin/password-field/theme/lumo/vaadin-password-field.js';
import '@vaadin/button/theme/lumo/vaadin-button.js';
import 'Frontend/generated/jar-resources/disableOnClickFunctions.js';
import '@vaadin/common-frontend/ConnectionIndicator.js';
import '@vaadin/vaadin-lumo-styles/color-global.js';
import '@vaadin/vaadin-lumo-styles/typography-global.js';
import '@vaadin/vaadin-lumo-styles/sizing.js';
import '@vaadin/vaadin-lumo-styles/spacing.js';
import '@vaadin/vaadin-lumo-styles/style.js';
import '@vaadin/vaadin-lumo-styles/vaadin-iconset.js';
import 'Frontend/generated/jar-resources/ReactRouterOutletElement.tsx';

const loadOnDemand = (key) => {
  const pending = [];
  if (key === 'eaaa3c90336c7e8297e03c9c796feb89b43cb47a5854760f7374abd366ce381f') {
    pending.push(import('./chunks/chunk-f803df643c07aa37eee57a440b68a91abb3cd3408a72bcc573024c04162131e4.js'));
  }
  if (key === 'f1285d8c281dc6ef5259bfdac8a9eac044fa62eff67c8b38ce501c91ba5e393c') {
    pending.push(import('./chunks/chunk-1c0f79919aba0603ad10f0145fcb2656e7eb00e3f092ae34012fa5ff7de78639.js'));
  }
  if (key === 'b9651b14b31d0da7262627dfd4035bf089e3c9bfe4ca351bc6c7ca80024ab9a6') {
    pending.push(import('./chunks/chunk-1c0f79919aba0603ad10f0145fcb2656e7eb00e3f092ae34012fa5ff7de78639.js'));
  }
  if (key === '0b065c58eff820419884816b5d967ad0336806b8b1c3bcaeda6632b061ef5dd6') {
    pending.push(import('./chunks/chunk-5723407bd39e04c6776c6a56e35ffac37b4aacf615d120d216b637f6c9cc37b6.js'));
  }
  if (key === 'b374fbd55cf42c21a1e5fa557e99101828d357fac74d00c7346c2f485b226111') {
    pending.push(import('./chunks/chunk-1c0f79919aba0603ad10f0145fcb2656e7eb00e3f092ae34012fa5ff7de78639.js'));
  }
  if (key === 'f227d13af71665abbea3b2ccd0aceb0063cd567214e8c0611a63040b355c87a3') {
    pending.push(import('./chunks/chunk-fe59c2e31323c47e0ac33d95f70423a453527b26b57bb6e385384d17d1500a07.js'));
  }
  if (key === 'f78a78f76724f971fc30050bb102dbcbc2e9c8fcd9e42071c246a17e323cac65') {
    pending.push(import('./chunks/chunk-1c0f79919aba0603ad10f0145fcb2656e7eb00e3f092ae34012fa5ff7de78639.js'));
  }
  if (key === '5c9a9b8119589fc6f31e03b628fee7779be2f95de5cb2354b728f6731031627a') {
    pending.push(import('./chunks/chunk-1c0f79919aba0603ad10f0145fcb2656e7eb00e3f092ae34012fa5ff7de78639.js'));
  }
  if (key === '1cec3bc79498004bc39e0ce0efcd93028eee0dcc6d71d57ecc988e61ea4c820c') {
    pending.push(import('./chunks/chunk-1c0f79919aba0603ad10f0145fcb2656e7eb00e3f092ae34012fa5ff7de78639.js'));
  }
  if (key === '4d5854d88fe59f22e1a7d281c696ecffff86ef1abb20cfe966e54416794803d1') {
    pending.push(import('./chunks/chunk-1c0f79919aba0603ad10f0145fcb2656e7eb00e3f092ae34012fa5ff7de78639.js'));
  }
  if (key === 'e7fae06c3dfe273ec338596fe26a50df0aaf1a8274b1a1d13bc9f7a674ddc458') {
    pending.push(import('./chunks/chunk-1c0f79919aba0603ad10f0145fcb2656e7eb00e3f092ae34012fa5ff7de78639.js'));
  }
  if (key === 'bc75763ca97ac70fb40a732d7d44a72fe08c209e434a386608900ef8fe80c9a7') {
    pending.push(import('./chunks/chunk-1c0f79919aba0603ad10f0145fcb2656e7eb00e3f092ae34012fa5ff7de78639.js'));
  }
  if (key === '55f4b325a56d94ef003ca8c6fa6210c60474e534469cf97e6dc379edd33ae114') {
    pending.push(import('./chunks/chunk-1c0f79919aba0603ad10f0145fcb2656e7eb00e3f092ae34012fa5ff7de78639.js'));
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