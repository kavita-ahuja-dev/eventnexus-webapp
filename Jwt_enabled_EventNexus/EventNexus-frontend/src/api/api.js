import axios from 'axios';

const BASE_URL = process.env.REACT_APP_API_BASE_URL ?? '/api';

const api = axios.create({
  baseURL: BASE_URL,
});

export const setAuthToken = (token) => {
  if (token) {
    api.defaults.headers.common.Authorization = `Bearer ${token}`;
    axios.defaults.headers.common.Authorization = `Bearer ${token}`;
  } else {
    delete api.defaults.headers.common.Authorization;
    delete axios.defaults.headers.common.Authorization;
  }
};

// Public endpoints that do NOT require token
const PUBLIC_PATHS = [
  '/auth/login',
  '/users/login',
  '/auth/register',
  '/customers/register',
  '/health',
  '/actuator/health',
  '/v3/api-docs',
  '/swagger-ui',
];

api.interceptors.request.use((config) => {
  const url = config.url || '';
  const method = (config.method || 'get').toLowerCase();
  const isPublic = PUBLIC_PATHS.some((p) => url === p || url.startsWith(p + '/') || url.startsWith(p + '?') || url.startsWith('/api' + p));
  const hasAuthHeader = !!api.defaults.headers.common.Authorization;

  try {
    if (method === 'get') {
      const u = new URL(config.url, window.location.origin);
      u.searchParams.set('_ts', Date.now().toString());
      config.url = u.pathname + u.search + u.hash;
    }
  } catch {}

  // eslint-disable-next-line no-console
  console.debug('[API]', method.toUpperCase(), (api.defaults.baseURL || '') + (config.url || ''));
  return config;
});

api.interceptors.response.use(
  (res) => res,
  (err) => {
    const status = err?.response?.status;
    // Only consider 401 (Unauthorized)
    if (status === 401) {
      // Look for strong signals that the JWT is bad/expired
      const www = String(err?.response?.headers?.['www-authenticate'] || '');
      const code = String(err?.response?.data?.code || '');
      const msg  = String(err?.response?.data?.message || '');

      const looksLikeBadToken =
        /invalid|expired|signature|malformed|not\s*before/i.test(www + ' ' + code + ' ' + msg);

      if (looksLikeBadToken) {
        try { localStorage.removeItem('token'); } catch {}
        setAuthToken(null);
      }
    }

    return Promise.reject(err);
  }
);


export default api;
