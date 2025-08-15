function base64UrlDecode(str) {
  try {
    const pad = '='.repeat((4 - (str.length % 4)) % 4);
    const b64 = (str + pad).replace(/-/g, '+').replace(/_/g, '/');
    const decoded = atob(b64);
    return new TextDecoder().decode(Uint8Array.from(decoded, c => c.charCodeAt(0)));
  } catch {
    return null;
  }
}

const CLOCK_SKEW_SEC = 5;

export function isTokenExpired(token) {
  if (typeof token !== 'string') return true;
  const parts = token.split('.');
  if (parts.length !== 3) return true;
  try {
    const payloadStr = base64UrlDecode(parts[1]);
    if (!payloadStr) return true;
    const payload = JSON.parse(payloadStr);
    const exp = Number(payload.exp);
    if (!Number.isFinite(exp)) return true;
    return Date.now() + CLOCK_SKEW_SEC * 1000 >= exp * 1000;
  } catch {
    return true;
  }
}

export function bootstrapAuthHeaders(axiosModule, apiInstance) {
  try {
    const token = localStorage.getItem('token');
    if (!token || isTokenExpired(token)) {
      localStorage.removeItem('token');
      delete axiosModule?.defaults?.headers?.common?.Authorization;
      delete apiInstance?.defaults?.headers?.common?.Authorization;
      return false;
    }
    if (axiosModule?.defaults?.headers?.common) {
      axiosModule.defaults.headers.common.Authorization = `Bearer ${token}`;
    }
    if (apiInstance?.defaults?.headers?.common) {
      apiInstance.defaults.headers.common.Authorization = `Bearer ${token}`;
    }
    return true;
  } catch {
    return false;
  }
}

