package org

import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.FilterConfig
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse

class MarkDownFilter implements Filter{


    @Override
    void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain){
        resp.writer.write('\nin thing\n')
        chain.doFilter(req, resp)

        resp.writer.write('\nout thing\n')

    }

    @Override
    void destroy() {

    }
}