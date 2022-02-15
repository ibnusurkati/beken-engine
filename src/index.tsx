import { EventSubscription, NativeEventEmitter, NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'beken-engine' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo managed workflow\n';

const BekenEngine = NativeModules.BekenEngine
  ? NativeModules.BekenEngine
  : new Proxy({}, {
    get() {
      throw new Error(LINKING_ERROR);
    },
  });

type AuthMitraPartner = {
  uuid: string
  name: string
  email: string
  phoneNumber: string
  publicKey: string
  secretKey: string
  debug?: boolean
}

type EmitterPayment = {
  status: boolean
  data: string
}

export default class Instance {
  public static open(params: AuthMitraPartner) {
    BekenEngine.open(JSON.stringify(params))
  }

  public static eventListener(name: string, listener: (data?: Object) => void): EventSubscription {
    const nativeEventEmitter = new NativeEventEmitter(BekenEngine)
      return nativeEventEmitter.addListener(name, event => {
        listener(JSON.parse(event.data))
      })
  }

  public static eventEmitter(name: string, emitterPayment: EmitterPayment): void {
    BekenEngine.push(name, emitterPayment.status, emitterPayment.data)
  }
}
